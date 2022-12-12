package com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bme.clp.bck.<%=resourceDot%>.q.domain.model.mongo.ModelDAO;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.model.AttributesDTO;

@Mapper
public interface MapperDTO {

  @Mapping(source = "id.attributeId", target = "attributeId")
  @Mapping(source = "attribute", target = "attribute")
  AttributesDTO toDTO(ModelDAO dao);

}