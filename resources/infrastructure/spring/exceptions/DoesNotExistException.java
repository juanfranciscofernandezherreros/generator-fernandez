package com.bme.clp.bck.resources.q.infrastructure.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bme.clp.bck.resources.q.web.api.model.ProblemType;
import com.bme.web.exception.BaseApiException;

import lombok.Getter;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class DoesNotExistException extends BaseApiException {

  private static final long serialVersionUID = 577827199728995276L;
  private static final String API_MESSAGE = "resource.not.found";
  private static final String API_CODE = "SERVICE-0000";
  private static final String ERROR_PREFIX = "El ";
  private static final String ERROR_SUFFIX = " no ha sido encontrado";
  private ProblemType problemType;

  public DoesNotExistException(final String resource, final String nombre) {
    super(ERROR_PREFIX + resource + " - " + nombre + ERROR_SUFFIX, null, API_CODE, API_MESSAGE,
      null);
  }

  public DoesNotExistException(final String resource, final String nombre,
    final ProblemType problemType) {
    super(ERROR_PREFIX + resource + " - " + nombre + ERROR_SUFFIX, null, API_CODE, API_MESSAGE,
      null);
    this.problemType = problemType;
  }

}