package com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bme.clp.bck.<%=resourceDot%>.q.domain.model.mongo.ModelPKDAO;
import com.bme.clp.bck.<%=resourceDot%>.q.domain.service.DomainService;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.UseCaseService;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.mapper.MapperDTO;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.model.AttributesDTO;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.FilterType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.RestrictionRootType;

public class UseCaseServiceImpl implements UseCaseService {

  private DomainService domainService;
  private MapperDTO mapperDto;

  public UseCaseServiceImpl(final DomainService domainService,
    final MapperDTO mapperDto) {
    this.domainService = domainService;
    this.mapperDto = mapperDto;
  }

  @Override
  public Page<AttributesDTO> search(
    final Integer pageNumber,
    final Integer pageSize,
    final String sort,
    final List<FilterType> filters,
    final List<RestrictionRootType> restrictions) {
    return domainService.search(pageNumber, pageSize, sort, filters, restrictions)
      .map(mapperDto::toDTO);
  }

  @Override
  public AttributesDTO findById(final String id) {
    ModelPKDAO pk = new ModelPKDAO();
    pk.setAttributeId(id);
    return mapperDto.toDTO(domainService.findById(pk));
  }

}