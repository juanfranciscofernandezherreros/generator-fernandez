package com.bme.clp.bck.resources.q.infrastructure.spring.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.bme.clp.bck.resources.q.domain.service.DomainService;
import com.bme.clp.bck.resources.q.domain.service.impl.DomainServiceImpl;
import com.bme.clp.bck.resources.q.infrastructure.spring.repository.mongo.ResourceMongoRepository;
import com.bme.clp.bck.resources.q.usecase.service.UseCaseService;
import com.bme.clp.bck.resources.q.usecase.service.impl.UseCaseServiceImpl;
import com.bme.clp.bck.resources.q.usecase.service.mapper.MapperDTO;
import com.bme.clp.bck.resources.q.web.api.AdvancedQueryApiDelegate;
import com.bme.clp.bck.resources.q.web.api.QueryApiDelegate;
import com.bme.clp.bck.resources.q.web.api.impl.AdvancedQueryApiDelegateImpl;
import com.bme.clp.bck.resources.q.web.api.impl.QueryApiDelegateImpl;
import com.bme.clp.bck.resources.q.web.mapper.JsonApiMapper;

@Configuration
@ComponentScan(basePackages = { "org.openapitools",
  "org.openapitools.configuration",
  "com.bme.clp.bck.resources.q.usecase.service.impl",
  "com.bme.clp.bck.resources.q.infrastructure",
  "com.bme.clp.bck.resources.q.web.mapper",
  "com.bme.clp.bck.resources.q.web.api",
  "com.bme.clp.bck.resources.q.infrastructure.spring" }
)
public class WebConfig {
    @Bean
    public QueryApiDelegate queryApiDelegate(final UseCaseService useCaseService,
      final JsonApiMapper jsonApiMapper) {
      return new QueryApiDelegateImpl(useCaseService, jsonApiMapper);
    }

    @Bean
    public AdvancedQueryApiDelegate advancedQueryApiDelegate(final UseCaseService useCaseService,
      final JsonApiMapper jsonApiMapper) {
      return new AdvancedQueryApiDelegateImpl(useCaseService, jsonApiMapper);
    }

    @Bean
    public UseCaseService useCaseService(final DomainService domainService,
      final MapperDTO mapperDto) {
      return new UseCaseServiceImpl(domainService, mapperDto);
    }

    @Bean
    public DomainService domainService(final MongoTemplate mongoTemplate,
      final ResourceMongoRepository repository) {
        return new DomainServiceImpl(mongoTemplate, repository);
    }

    @Bean
    public JsonApiMapper jsonApiMapper() {
      return Mappers.getMapper(JsonApiMapper.class);
    }

    @Bean
    public MapperDTO mapperDto() {
      return Mappers.getMapper(MapperDTO.class);
    }
}