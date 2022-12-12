package com.bme.clp.bck.<%=resourceDot%>.q.domain.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bme.clp.bck.<%=resourceDot%>.q.domain.model.mongo.ModelDAO;
import com.bme.clp.bck.<%=resourceDot%>.q.domain.model.mongo.ModelPKDAO;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.FilterType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.RestrictionRootType;

public interface DomainService {

  Page<ModelDAO> search(
    Integer pageNumber,
    Integer pageSize,
    String sort,
    List<FilterType> listFilters,
    List<RestrictionRootType> listRestrictionRootTypes);

  ModelDAO findById(ModelPKDAO ModelPKDAO);

}