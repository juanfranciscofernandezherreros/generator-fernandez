package com.bme.clp.bck.resources.q.usecase.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bme.clp.bck.resources.q.usecase.service.model.AttributesDTO;
import com.bme.clp.bck.resources.q.web.api.model.FilterType;
import com.bme.clp.bck.resources.q.web.api.model.RestrictionRootType;

public interface UseCaseService {

  Page<AttributesDTO> search(Integer pageNumber, Integer pageSize, String sort,
    List<FilterType> filters, List<RestrictionRootType> restrictions);

  AttributesDTO findById(String id);

}