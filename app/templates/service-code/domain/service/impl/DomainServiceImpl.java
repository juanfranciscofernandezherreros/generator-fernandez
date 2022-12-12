package com.bme.clp.bck.<%=resourceDot%>.q.domain.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import com.bme.clp.bck.<%=resourceDot%>.q.domain.model.mongo.ModelDAO;
import com.bme.clp.bck.<%=resourceDot%>.q.domain.model.mongo.ModelPKDAO;
import com.bme.clp.bck.<%=resourceDot%>.q.domain.service.DomainService;
import com.bme.clp.bck.<%=resourceDot%>.q.domain.utils.Constants;
import com.bme.clp.bck.<%=resourceDot%>.q.domain.utils.UtilsCommon;
import com.bme.clp.bck.<%=resourceDot%>.q.domain.utils.UtilsMappings;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.exceptions.DoesNotExistException;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.repository.mongo.ResourceMongoRepository;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.FilterType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.ProblemDetailType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.ProblemType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.RestrictionRootType;
import com.bme.clp.bck.query.lib.domain.query.ComplexQuery;
import com.bme.clp.bck.query.lib.domain.query.Sorting;

public class DomainServiceImpl implements DomainService {

  private MongoTemplate mongoTemplate;
  private ResourceMongoRepository repository;

  @Value("${pagination.defaultSortField}")
  private String defaultSortField;
  @Value("${pagination.defaultPageSize}")
  private Integer defaultPageSize;
  @Value("${pagination.defaultPageNumber}")
  private Integer defaultPageNumber;

  public DomainServiceImpl(final MongoTemplate mongoTemplate, final ResourceMongoRepository repository) {
    this.mongoTemplate = mongoTemplate;
    this.repository = repository;
  }

  @Override
  public Page<ModelDAO> search(
    final Integer pageNumber,
    final Integer pageSize,
    final String sort,
    final List<FilterType> listFilterTypes,
    final List<RestrictionRootType> listRestrictionRootTypes) {
    List<Class> listClasses = Arrays.asList(ModelDAO.class, ModelPKDAO.class);
    List<String> fieldsInAllClasses = UtilsCommon.listOfAllFieldsInClasses(listClasses);
    List<FilterType> listFilterTypesMapped = (listFilterTypes != null && !listFilterTypes.isEmpty())
      ? UtilsMappings.mappingFilter(listFilterTypes)
      : new ArrayList<FilterType>();
    List<RestrictionRootType> listRestrictionRootTypesMapped = (listRestrictionRootTypes != null
      && !listRestrictionRootTypes.isEmpty())
      ? UtilsMappings.mappingRestrictions(listRestrictionRootTypes)
      : new ArrayList<RestrictionRootType>();
    UtilsCommon.throwInvalidParams(listFilterTypesMapped,
      listRestrictionRootTypesMapped,
      fieldsInAllClasses);
    ComplexQuery complexQuery = new ComplexQuery();
    complexQuery
      .addFiltersAndRestrictions(listFilterTypesMapped,
        listRestrictionRootTypesMapped,
        Arrays.asList(ModelDAO.class, ModelPKDAO.class))
      .withSorting(UtilsMappings.mappingSorting(sort),
        ModelDAO.class, ModelPKDAO.class,
        UtilsMappings.mappingSorting(defaultSortField));
    Pageable pageable = PageRequest.of((pageNumber == null) ? defaultPageNumber : pageNumber - 1,
      (pageSize == null) ? defaultPageSize : pageSize,
      Sorting.sortBy(UtilsMappings.mappingSorting(sort), ModelDAO.class,
        ModelPKDAO.class,
        UtilsMappings.mappingSorting(defaultSortField)));
    long count = mongoTemplate.count(complexQuery, ModelDAO.class);
    complexQuery.with(pageable);
    return new PageImpl<ModelDAO>(mongoTemplate
      .find(complexQuery, ModelDAO.class)
      .stream().collect(Collectors.toList()), pageable,
      count);
  }

  @Override
  public ModelDAO findById(final ModelPKDAO ModelPKDAO) {
     List<ProblemDetailType> listProblemDetailType = new ArrayList<ProblemDetailType>();
     ProblemDetailType problemDetailType = new ProblemDetailType();
     problemDetailType.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
     problemDetailType.setStatus(Integer.valueOf(HttpStatus.NOT_FOUND.value()));
     problemDetailType.setInstance(Constants.INSTANCE);
     problemDetailType.setDetail(Constants.RESOURCE_NOT_FOUND);
     listProblemDetailType.add(problemDetailType);
     ProblemType problemType = new ProblemType();
     problemType.addErrorsItem(problemDetailType);
     return repository.findById(ModelPKDAO)
       .orElseThrow(() -> new DoesNotExistException(Constants.INSTANCE,
         ModelPKDAO.getAttributeId(),
         problemType));
  }
}
