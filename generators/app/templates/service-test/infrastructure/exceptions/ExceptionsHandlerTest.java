package com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.exceptions;

import com.bme.clp.bck.<%=resourceDot%>.q.domain.model.mongo.ModelDAO;
import com.bme.clp.bck.<%=resourceDot%>.q.domain.utils.Constants;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.exceptions.BadRequestException;
import com.bme.clp.bck.<%=resourceDot%>.q.infrastructure.spring.exceptions.ExceptionsHandler;
import com.bme.clp.bck.<%=resourceDot%>.q.web.api.model.ProblemType;
import com.bme.clp.bck.query.lib.infrastructure.exceptions.SortingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"integration-test"})
class ExceptionsHandlerTest {

  @InjectMocks
  ExceptionsHandler exceptionsHandler;
  @Mock
  WebExchangeBindException webExchangeBindException;
  @Mock
  BindingResult bindingResult;

  @Test
  void process_exception() {
    Exception ex = new Exception();
    // GIVEN
    // WHEN
    // THEN
    ResponseEntity<ProblemType> result = exceptionsHandler.handleException(ex);
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void process_webExchangeBindException_without_listErrors() {
    // GIVEN
    BindingResult bindingResult = Mockito.mock(BindingResult.class);
    List<ObjectError> errorsList = Collections.emptyList();
    Mockito.when(bindingResult.getAllErrors()).thenReturn(errorsList);
    WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);
    // WHEN
    ResponseEntity<ProblemType> result = exceptionsHandler.handleWebExchangeBindException(ex);
    // THEN
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void process_webExchangeBindException_with_listErrors() {
    // GIVEN
    BindingResult bindingResult = Mockito.mock(BindingResult.class);
    List<ObjectError> errorsList = new ArrayList<ObjectError>();
    DefaultMessageSourceResolvable d = new DefaultMessageSourceResolvable("default_message_1");
    Object[] arrayObject = new Object[] {
      d
    };
    String[] codes = new String[] {
      "code_1"
    };
    ObjectError error = new ObjectError("fakeName", codes, arrayObject, "Description of error");
    errorsList.add(error);
    Mockito.when(bindingResult.getAllErrors()).thenReturn(errorsList);
    Object target = new ModelDAO();
    Mockito.when(bindingResult.getTarget()).thenReturn(target);
    WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);
    // WHEN
    ResponseEntity<ProblemType> result = exceptionsHandler.handleWebExchangeBindException(ex);
    // THEN
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void process_badRequestException() {
    BadRequestException ex = new BadRequestException("bad request", "fake",
      Constants.MSG_ERROR_RESOURCE_NOT_FOUND, new ProblemType());
    // GIVEN
    // WHEN
    ResponseEntity<ProblemType> result = exceptionsHandler.handleBadRequest(ex);
    // THEN
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  void test_handleSortingException() {
    // GIVEN
    List<String> listFieldsNotFound = new ArrayList<String>();
    listFieldsNotFound.add("mockField");
    SortingException sortingException = new SortingException(listFieldsNotFound);
    // WHEN
    ResponseEntity<ProblemType> result = exceptionsHandler.handleSortingException(sortingException);
    // THEN
    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    assertTrue(!result.getBody().getErrors().isEmpty());
  }
}
