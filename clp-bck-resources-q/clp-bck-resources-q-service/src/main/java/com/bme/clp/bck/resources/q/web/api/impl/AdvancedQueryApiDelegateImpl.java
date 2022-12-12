package com.bme.clp.bck.resources.q.web.api.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;

import com.bme.clp.bck.resources.q.usecase.service.UseCaseService;
import com.bme.clp.bck.resources.q.web.api.AdvancedQueryApiDelegate;
import com.bme.clp.bck.resources.q.web.api.model.DataColResponseType;
import com.bme.clp.bck.resources.q.web.api.model.QueryType;
import com.bme.clp.bck.resources.q.web.mapper.JsonApiMapper;

import reactor.core.publisher.Mono;

public class AdvancedQueryApiDelegateImpl implements AdvancedQueryApiDelegate {

  private UseCaseService useCaseService;
  private JsonApiMapper jsonApiMapper;

  public AdvancedQueryApiDelegateImpl(final UseCaseService useCaseService,
    final JsonApiMapper jsonApiMapper) {
    this.useCaseService = useCaseService;
    this.jsonApiMapper = jsonApiMapper;
  }

  @Override
  public Mono<ResponseEntity<DataColResponseType>> search(
    final Integer pageSize,
    final Integer pageNumber,
    final String sort,
    final Mono<QueryType> queryType,
    final ServerWebExchange exchange) {
    return queryType.map(r -> ResponseEntity.ok(
     jsonApiMapper.toPagesDataColResponseType(
          useCaseService.search(pageNumber, pageSize, sort,
            r.getFilters(),
            r.getRestrictions()),
          exchange)))
      .defaultIfEmpty(ResponseEntity.ok(
        jsonApiMapper.toPagesDataColResponseType(
          useCaseService.search(pageNumber, pageSize, sort, null,
            null),
          exchange)));
  }

}
