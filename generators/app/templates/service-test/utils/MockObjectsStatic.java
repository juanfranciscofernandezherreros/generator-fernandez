package com.bme.clp.bck.<%=resourceDot%>.q.utils;

import java.io.IOException;
import java.nio.file.Paths;

import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.DataColResponseType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.DataResponseType;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.QueryType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class MockObjectsStatic {

  private static final String PATH_QUERY = "src/test/resources/json/query/";
  private static final String PATH_OBJECT = "src/test/resources/json/objects/";

  public Mono<QueryType> buildMonoQueryType() {
    return Mono.justOrEmpty(new QueryType());
  }

  public Mono<QueryType> buildCaseQuery(final String jsonName) throws
    JsonParseException,
    JsonMappingException,
    IOException {

    ObjectMapper mapper = new ObjectMapper();

    return Mono.justOrEmpty( mapper.
            readValue(Paths.get(PATH_QUERY + jsonName).toFile(),
                    QueryType.class));
  }

  public DataColResponseType buildCaseObject(final String jsonName) throws
    JsonParseException,
    JsonMappingException,
    IOException {

    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(Paths.get(PATH_OBJECT + jsonName).toFile(),
        DataColResponseType.class);
  }

  public DataResponseType buildCaseGet(final String jsonName) throws
    JsonParseException,
    JsonMappingException,
    IOException {

    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(Paths.get(PATH_OBJECT + jsonName).toFile(),
      DataResponseType.class);
 }
}
