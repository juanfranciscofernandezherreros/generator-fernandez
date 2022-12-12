package com.bme.clp.bck.<%=resourceDot%>.q.web.api.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.UseCaseService;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.model.AttributesDTO;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.QueryApiDelegate;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.DataColResponseType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.DataResponseType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.mapper.JsonApiMapper;
import reactor.core.publisher.Mono;

public class QueryApiDelegateImpl implements QueryApiDelegate {

  private UseCaseService useCaseService;
  private JsonApiMapper jsonApiMapper;

  public QueryApiDelegateImpl(final UseCaseService useCaseService, final JsonApiMapper jsonApiMapper) {
    this.useCaseService = useCaseService;
    this.jsonApiMapper = jsonApiMapper;
  }

  @Override
  public Mono<ResponseEntity<DataResponseType>> read(final String id,
          final ServerWebExchange exchange) {
    AttributesDTO dto = useCaseService.findById(id);
    DataResponseType dataResponseType = jsonApiMapper.toDataResponseType(dto);
    return Mono.just(ResponseEntity.ok(dataResponseType));
  }

  @Override
  public Mono<ResponseEntity<DataColResponseType>> readCollection(final Integer pageSize,
      final Integer pageNumber,
      final String sort,
      final ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(
            jsonApiMapper.toPagesDataColResponseType(
                 useCaseService.search(pageNumber, pageSize, sort,
                   null,
                   null),
                 exchange)))
             .defaultIfEmpty(ResponseEntity.ok(
               jsonApiMapper.toPagesDataColResponseType(
                 useCaseService.search(pageNumber, pageSize, sort, null,
                   null),
                 exchange)));
  }
}
