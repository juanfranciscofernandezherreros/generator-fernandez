package com.bme.clp.bck.resources.q.infrastructure.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.bme.clp.bck.resources.q.web.api.model.ProblemType;
import com.bme.web.exception.BaseApiException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseApiException {

  private static final long serialVersionUID = -5256321253101591020L;
  private static final String API_MESSAGE = "resource.not.found";
  private static final String API_CODE = "SERVICE-0000";
  private static final String ERROR_PREFIX = "The ";
  private static final String ERROR_SUFFIX = " has not been found";
  private ProblemType problemType;

  public NotFoundException(final String resource, final String nombre,
    final ProblemType problemType) {
    super(ERROR_PREFIX + resource + " - " + nombre + ERROR_SUFFIX, null, API_CODE, API_MESSAGE,
      null);
    this.problemType = problemType;
  }
}
