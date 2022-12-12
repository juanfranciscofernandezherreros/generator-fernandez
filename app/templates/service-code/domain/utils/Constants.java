package com.bme.clp.bck.<%=resourceDot%>.q.domain.utils;

public class Constants {

  public static final String INSTANCE = "<%=resource%>";
  public static final String TYPE_URL = "/<%=resource%>/search";
  public static final String MICROSERVICE_NAME = "clp-bck-<%=resource%>-q";

  public static final String MSG_ERROR_RESOURCE_NOT_FOUND = "Resource can't be found";
  public static final String MSG_ERROR_FILTER_NOT_EXIST = "Filter field does not exist";
  public static final String MSG_ERROR_RESTRICTION_MUST_BE_NUMBER = "The restriction field must be numeric";
  public static final String MSG_ERROR_DATE = "{0} with {1} is not correct";
  public static final String MSG_ERROR_RESTRICTION_NOT_EXIST = "Restriction field does not exist";
  public static final String TYPE_OF_STRING = "STRING";
  public static final String TYPE_OF_BOOLEAN = "BOOLEAN";

  public static final int LENGHT_SHORT_DATE = 8;

  public static final String MSG_ERROR_REQUEST_PARAMETERS = "Your request parameters didn't validate";
  public static final String MSG_ERROR_RESTRICTION_NONE = "Restriction none has more than one value";
  public static final String MSG_ERROR_FIELD_REASON = "Parameters doesn't exist";
  public static final String ABOUTBLANK = "about:blank";

  public static final String DATE_SEPARATOR = "-";
  public static final int START_POSITION_OF_YEAR = 0;
  public static final int END_POSITION_OF_YEAR = 4;
  public static final int START_POSITION_OF_MONTH = 4;
  public static final int END_POSITION_OF_MONTH = 6;
  public static final int START_POSITION_OF_DAY = 6;
  public static final int END_POSITION_OF_DAY = 8;
  public static final String SUFIX_DATE_ISO_INSTANT = "T00:00:00.000Z";
  public static final String RESOURCE_NOT_FOUND = "Resource not found";

}