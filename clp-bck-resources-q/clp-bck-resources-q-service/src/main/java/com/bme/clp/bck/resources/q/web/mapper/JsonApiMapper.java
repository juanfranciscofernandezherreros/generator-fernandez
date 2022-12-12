package com.bme.clp.bck.resources.q.web.mapper;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ServerWebExchange;
import com.bme.clp.bck.resources.q.domain.utils.Constants;
import com.bme.clp.bck.resources.q.usecase.service.model.AttributesDTO;
import com.bme.clp.bck.resources.q.web.api.model.AttributesIdType;
import com.bme.clp.bck.resources.q.web.api.model.DataColResponseType;
import com.bme.clp.bck.resources.q.web.api.model.DataResponseType;
import com.bme.clp.bck.resources.q.web.api.model.LinksPaginationType;
import com.bme.clp.bck.resources.q.web.api.model.MetaType;
import com.bme.clp.bck.resources.q.web.api.model.ResourceType;

@Mapper
public interface JsonApiMapper {

  public static final String defaultSortField = "+attributeId";

  @Named("attributesIdType")
  AttributesIdType toAttributesIdType(
    AttributesDTO dto);

  @Mapping(constant = Constants.INSTANCE, target = "type")
  @Mapping(source = "idResponse", target = "id")
  @Mapping(source = ".", target = "attributes", qualifiedByName = "attributesIdType")
  ResourceType toResourceType(AttributesDTO dto);

  default DataColResponseType toPagesDataColResponseType(
    @NotNull Page<AttributesDTO> pageColResponse, ServerWebExchange exchange) {
    MetaType meta = new MetaType();
    meta.setPage(pageColResponse.getNumber() + 1);
    meta.setTotal((int) pageColResponse.getTotalElements());
    meta.setTotalPages(pageColResponse.getTotalPages());
    DataColResponseType dataColResponseType = new DataColResponseType();
    dataColResponseType.setMeta(meta);
    dataColResponseType
      .setLinks(buildLinks(exchange, pageColResponse.getTotalPages(),
        pageColResponse.getNumber() + 1, pageColResponse.getSize()));
    dataColResponseType.setData(
      pageColResponse.getContent().stream().map(this::toResourceType)
        .collect(Collectors.toList()));
    return dataColResponseType;
  }

  private LinksPaginationType buildLinks(final ServerWebExchange exchange, final int lastPage,
    final int currentPage, final int pageSize) {
    LinksPaginationType links = new LinksPaginationType();
    URI uri = exchange.getRequest().getURI();
    String[] uriParts = uri.toString().split("\\?");
    String buildParametersToURI = "";
    String first = exchange.getRequest().getPath().toString();
    String last = exchange.getRequest().getPath().toString();
    String next = exchange.getRequest().getPath().toString();
    String prev = exchange.getRequest().getPath().toString();
    buildParametersToURI = buildParametersToURI.concat("?page[size]=_1_");
    buildParametersToURI = buildParametersToURI.concat("&page[number]=_2_");
    var wrapper = new Object() {
      String match = "&sort=" + defaultSortField;
    };
    if (uriParts.length > 1) {
      String[] parts = uriParts[1].split("&");
      Arrays.asList(parts).forEach(p -> {
        if (p.contains("sort")) {
          wrapper.match = "&" + p;
        }
      });
    }
    buildParametersToURI = buildParametersToURI.concat(wrapper.match);
    if (lastPage > 0) {
      first = first
        .concat(buildParametersToURI.replace("_1_", String.valueOf(pageSize)).replace("_2_", "1"));
      last = last.concat(buildParametersToURI.replace("_1_", String.valueOf(pageSize))
        .replace("_2_", String.valueOf(lastPage)));
      next = (lastPage <= currentPage) ? null
        : next.concat(buildParametersToURI.replace("_1_", String.valueOf(pageSize))
        .replace("_2_", String.valueOf(currentPage + 1)));
      prev = (currentPage == 1 || currentPage - 1 > lastPage) ? null
        : prev.concat(buildParametersToURI.replace("_1_", String.valueOf(pageSize))
        .replace("_2_", String.valueOf(currentPage - 1)));
    } else {
      first = null;
      last = null;
      next = null;
      prev = null;
    }
    links.setFirst(first);
    links.setNext(next);
    links.setLast(last);
    links.setPrev(prev);
    return links;
  }

  default DataResponseType toDataResponseType(AttributesDTO dto) {
    DataResponseType retVal = new DataResponseType();
    retVal.data(toResourceType(dto));
    return retVal;
  };

}
