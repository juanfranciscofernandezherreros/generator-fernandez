package com.bme.clp.bck.<%=resourceDot%>.q.web.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.server.ServerWebExchange;

import com.bme.clp.bck.<%=resourceDot%>.q.domain.service.DomainService;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.config.MongoConfigurer;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.config.WebConfig;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.exceptions.BadRequestException;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.repository.mongo.ResourceMongoRepository;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.UseCaseService;
import com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.mapper.MapperDTO;
import com.bme.clp.bck.<%=resourceDot%>.q.utils.MockObjectsStatic;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.AdvancedQueryApiDelegate;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.DataColResponseType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.mapper.JsonApiMapper;
import com.bme.clp.bck.<%=resourceDot%>.q.web.tests.hooks.SetupMongod;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SetupMongod.class)
@ActiveProfiles({"integration-test"})
@SpringBootTest
@ContextConfiguration(classes = MongoConfigurer.class)
@TestMethodOrder(OrderAnnotation.class)
class TestReadApiObjects {
  private MockObjectsStatic mockStatic = new MockObjectsStatic();

  @Mock
  ServerWebExchange exchange;

  private AdvancedQueryApiDelegate advancedQueryApiDelegate;

  private static final int CURRENT_PAGE = 1;

  private static final String HOST = "https://api.test.org";

  private static final String URI = "/<%=resource%>/search";

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
    ResourceMongoRepository resourceMongoRepository = null;
    JsonApiMapper jsonApiMapper = config.jsonApiMapper();
    DomainService domainService = config.domainService(mongoTemplate, resourceMongoRepository);
    MapperDTO mapperDTO = config.mapperDto();
    UseCaseService useCaseService = config.useCaseService(domainService,
        mapperDTO);
    advancedQueryApiDelegate = config.advancedQueryApiDelegate(useCaseService, jsonApiMapper);
  }

  @DisplayName("Test with empty query")
  @Test
  @Order(Order.DEFAULT)
  void testEmptyQuery() {
    ResponseEntity<DataColResponseType> assertResult = advancedQueryApiDelegate
        .search(1, 1, "+attributeId", mockStatic.buildMonoQueryType(), this.getExchange()
            )
        .block();
    assertResult.getBody().getData().forEach(object -> {
      assertEquals("<%=resource%>", object.getType());
      assertNotNull(object.getId());
      assertNotNull(object.getAttributes());
      assertNotNull(object.getAttributes().getAttribute());
      assertNotNull(object.getAttributes().getAttributeId());
    });
    assertEquals(CURRENT_PAGE, assertResult.getBody().getMeta().getPage());
    assertNotNull(assertResult.getBody().getLinks().getFirst());
    assertNotNull(assertResult.getBody().getLinks().getLast());
    assertEquals(HttpStatus.OK, assertResult.getStatusCode());
  }

  @DisplayName("Test with query: query.json")
  @Test
  @Order(Order.DEFAULT)
  void testQuery() throws JsonParseException, JsonMappingException, IOException {
      ResponseEntity<DataColResponseType> assertResult = advancedQueryApiDelegate
            .search(1, 1, "+attributeId", mockStatic.buildMonoQueryType(), this.getExchange()
                )
            .block();
    assertEquals(HttpStatus.OK, assertResult.getStatusCode());
  }

  @DisplayName("Test with query: query_error.json")
  @Test
  @Order(Order.DEFAULT)
  void testQueryError() throws JsonParseException, JsonMappingException, IOException {
    try {
        advancedQueryApiDelegate
           .search(1, 1, "+attributeId", mockStatic.buildMonoQueryType(), this.getExchange()
                )
           .block();
    } catch (BadRequestException e) {
      assertEquals(e.getApiMessage(), "resource.not.found");
    }
  }

  @DisplayName("Test with empty query order set")
  @Test
  @Order(Order.DEFAULT)
  void testObjectOrderer() throws JsonParseException, JsonMappingException, IOException {
    ResponseEntity<DataColResponseType> assertResult = advancedQueryApiDelegate
            .search(1, 1, "+attributeId", mockStatic.buildMonoQueryType(), this.getExchange()
                )
            .block();
    assertEquals(HttpStatus.OK, assertResult.getStatusCode());
    assertResult = advancedQueryApiDelegate
            .search(1, 1, "-attributeId", mockStatic.buildMonoQueryType(), this.getExchange()
                )
            .block();
    assertEquals(HttpStatus.OK, assertResult.getStatusCode());

  }

  @DisplayName("Test and compare")
  @Test
  @Order(Order.DEFAULT)
  void testObjectPage() throws JsonParseException, JsonMappingException, IOException {
    ResponseEntity<DataColResponseType> assertResult = advancedQueryApiDelegate
            .search(1, 1, "+attributeId", mockStatic.buildMonoQueryType(), this.getExchange()
                )
            .block();
    assertEquals(HttpStatus.OK, assertResult.getStatusCode());

    //Read Test Object
    DataColResponseType response = mockStatic.buildCaseObject("test_object.json");

    //Compare with the value
    assertEquals(response, assertResult.getBody());
  }

}