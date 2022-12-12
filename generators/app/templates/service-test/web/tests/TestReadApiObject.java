package com.bme.clp.bck.<%=resourceDot%>.q.web.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ServerWebExchange;

import com.bme.clp.bck.<%=resourceDot%>.q.domain.service.DomainService;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.config.MongoConfigurer;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.config.WebConfig;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.exceptions.DoesNotExistException;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.repository.mongo.ResourceMongoRepository;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.UseCaseService;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.mapper.MapperDTO;
import com.bme.clp.bck.<%=resourceDot%>.q.utils.MockObjectsStatic;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.QueryApiDelegate;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.DataResponseType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.mapper.JsonApiMapper;
import com.bme.clp.bck.<%=resourceDot%>.q.web.tests.hooks.SetupMongod;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SetupMongod.class)
@SpringBootTest
@ContextConfiguration(classes = MongoConfigurer.class)
@TestMethodOrder(OrderAnnotation.class)

class TestReadApiObject {
  @Mock
  ServerWebExchange exchange;

  @Autowired
  ResourceMongoRepository resourceMongoRepository;

  private QueryApiDelegate queryApiDelegate;

  private static final String HOST = "https://api.test.org";

  private static final String URI = "/foreign-exchange/search";

  @Autowired
  @Qualifier("MongoTemplate")
  private MongoTemplate mongoTemplate;

  protected ServerWebExchange getExchange() {
      MockServerHttpRequest request = MockServerHttpRequest
              .post(HOST + URI)
              .header("X-CF-Forwarded-Url", HOST).build();
      return MockServerWebExchange.from(request);
  }

  @BeforeEach
  void setup() throws Exception {

    WebConfig config = new WebConfig();

    MapperDTO mapperDto = config.mapperDto();
    DomainService domainService = config.domainService(mongoTemplate, resourceMongoRepository);
    UseCaseService useCaseService = config.useCaseService(domainService, mapperDto);
    JsonApiMapper jsonApiMapper = config.jsonApiMapper();

    queryApiDelegate = config.queryApiDelegate(useCaseService, jsonApiMapper);
  }

  @DisplayName("Test exists object")
  @Test
  void testObjectPage() throws JsonParseException, JsonMappingException, IOException, ParseException {
    resourceMongoRepository.count();

    ResponseEntity<DataResponseType> result =
        queryApiDelegate.read("1", exchange).block();

    assertEquals(HttpStatus.OK, result.getStatusCode());

    DataResponseType compare = new MockObjectsStatic().buildCaseGet("test_object_query.json");

    assertEquals(compare, result.getBody());
  }

  @DisplayName("Test does not exist object")
  @Test
  void testObjectNotFound() throws JsonParseException, JsonMappingException, IOException, ParseException {
       assertThrows(DoesNotExistException.class,
         () -> queryApiDelegate.read("NPE", exchange).block());
  }
}