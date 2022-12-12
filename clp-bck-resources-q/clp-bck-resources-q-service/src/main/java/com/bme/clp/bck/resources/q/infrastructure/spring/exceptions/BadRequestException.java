package com.bme.clp.bck.resources.q.infrastructure.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bme.clp.bck.resources.q.web.api.model.ProblemType;
import com.bme.web.exception.BaseApiException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseApiException {

  private static final long serialVersionUID = 577827199728995276L;
  private static final String API_MESSAGE = "resource.not.found";
  private static final String API_CODE = "SERVICE-0000";
  private static final String ERROR_PREFIX = "The ";
  private ProblemType problemType;

  /**
   * @param resource
   * @param nombre
   * @param problemType
   */
  public BadRequestException(final String resource, final String nombre, final String ERROR_SUFFIX,
    final ProblemType problemType) {
    super(ERROR_PREFIX + resource + " - " + nombre + ERROR_SUFFIX, null, API_CODE, API_MESSAGE,
      null);
    this.problemType = problemType;
  }

  public ProblemType getProblemType() {
    return problemType;
  }
}
