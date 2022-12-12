package com.bme.clp.bck.resources.q.usecase.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bme.clp.bck.resources.q.domain.model.mongo.ModelDAO;
import com.bme.clp.bck.resources.q.usecase.service.model.AttributesDTO;

@Mapper
public interface MapperDTO {

  @Mapping(source = "id.attributeId", target = "attributeId")
  @Mapping(source = "attribute", target = "attribute")
  AttributesDTO toDTO(ModelDAO dao);

}