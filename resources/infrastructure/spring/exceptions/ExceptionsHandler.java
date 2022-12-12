package com.bme.clp.bck.resources.q.infrastructure.spring.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.bme.clp.bck.resources.q.domain.utils.Constants;
import com.bme.clp.bck.resources.q.web.api.model.ProblemDetailType;
import com.bme.clp.bck.resources.q.web.api.model.ProblemParamType;
import com.bme.clp.bck.resources.q.web.api.model.ProblemType;
import com.bme.clp.bck.query.lib.infrastructure.exceptions.SortingException;

@ControllerAdvice
public class ExceptionsHandler {

  Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseBody
  public ResponseEntity<ProblemType> handleWebExchangeBindException(
    final WebExchangeBindException ex) {
    logger.error(messageLog(ex.getBindingResult().getAllErrors().toString(),
      "handleWebExchangeBindException"));
    ProblemType problemType = new ProblemType();
    problemType.setErrors(new ArrayList<>());
    ProblemDetailType problemDetailType = new ProblemDetailType();
    List<ProblemParamType> invalidParams = new ArrayList<>();
    ex.getBindingResult().getAllErrors().forEach(e -> {
      ProblemParamType problemParamType = new ProblemParamType();
      problemParamType
        .setName(
          ((org.springframework.context.support.DefaultMessageSourceResolvable) e.getArguments()[0])
            .getDefaultMessage());
      problemParamType.setReason(e.getDefaultMessage());
      invalidParams.add(problemParamType);
      problemDetailType.setInstance(ex.getBindingResult().getTarget().toString());
    });
    problemDetailType.setInvalidParams(invalidParams);
    problemDetailType.setStatus(HttpStatus.BAD_REQUEST.value());
    problemDetailType.setTitle(Constants.MSG_ERROR_REQUEST_PARAMETERS);
    problemDetailType.setType(Constants.ABOUTBLANK);
    problemType.getErrors().add(problemDetailType);
    return new ResponseEntity<>(problemType, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseBody
  public ResponseEntity<ProblemType> handleNotFoundException(
    final NotFoundException ex) {
    logger.error(messageLog(ex.getMessage(), "handleNotFoundException"));
    return new ResponseEntity<>(ex.getProblemType(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ProblemType> handleException(final Exception ex) {
    logger.error(messageLog(ex.getMessage(), "Exception"));
    List<ProblemDetailType> listProblemDetailType = new ArrayList<>();
    ProblemDetailType problemDetailType = new ProblemDetailType();
    problemDetailType.setType(Constants.ABOUTBLANK);
    problemDetailType.setTitle(Constants.MSG_ERROR_REQUEST_PARAMETERS);
    problemDetailType.setStatus(Integer.valueOf(HttpStatus.BAD_REQUEST.value()));
    problemDetailType.setInstance(Constants.INSTANCE);
    listProblemDetailType.add(problemDetailType);
    ProblemType problemType = new ProblemType();
    problemType.addErrorsItem(problemDetailType);
    return new ResponseEntity<>(problemType, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseBody
  public ResponseEntity<ProblemType> handleBadRequest(
    final BadRequestException ex) {
    return new ResponseEntity<>(ex.getProblemType(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(SortingException.class)
  @ResponseBody
  public ResponseEntity<ProblemType> handleSortingException(
    final SortingException ex) {
    logger.error(messageLog(ex.getMessage(), "handleSortingException"));
    List<ProblemDetailType> listProblemDetailType = new ArrayList<ProblemDetailType>();
    ProblemDetailType problemDetailType = new ProblemDetailType();
    problemDetailType.setTitle(Constants.MSG_ERROR_REQUEST_PARAMETERS);
    problemDetailType.setStatus(Integer.valueOf(HttpStatus.BAD_REQUEST.value()));
    problemDetailType.setInvalidParams(ex.getListFieldsNotFound().stream()
      .map(this::buildProblemParamType).collect(Collectors.toList()));
    listProblemDetailType.add(problemDetailType);
    ProblemType model404ProblemType = new ProblemType();
    model404ProblemType.addErrorsItem(problemDetailType);
    return new ResponseEntity<>(model404ProblemType, HttpStatus.BAD_REQUEST);
  }

  private ProblemParamType buildProblemParamType(final String field) {
    ProblemParamType problemParamType = new ProblemParamType();
    problemParamType.setName(field);
    problemParamType.setReason(Constants.MSG_ERROR_FIELD_REASON);
    return problemParamType;
  }

  private String messageLog(final String logTrace, final String explanatoryMessage) {
    // Format <application><log trace><explanatory message>
    StringBuilder men = new StringBuilder();
    men.append(Constants.MICROSERVICE_NAME).append("<").append(logTrace).append(">")
      .append("<").append(explanatoryMessage).append(">");
    return men.toString();
  }
}
