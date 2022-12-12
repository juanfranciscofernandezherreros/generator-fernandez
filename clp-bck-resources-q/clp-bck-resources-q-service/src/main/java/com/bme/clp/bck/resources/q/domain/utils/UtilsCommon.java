package com.bme.clp.bck.resources.q.domain.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import com.bme.clp.bck.resources.q.infrastructure.spring.exceptions.BadRequestException;
import com.bme.clp.bck.resources.q.web.api.model.FilterType;
import com.bme.clp.bck.resources.q.web.api.model.ProblemDetailType;
import com.bme.clp.bck.resources.q.web.api.model.ProblemParamType;
import com.bme.clp.bck.resources.q.web.api.model.ProblemType;
import com.bme.clp.bck.resources.q.web.api.model.RestrictionRootType;
import io.micrometer.core.instrument.util.StringUtils;

public class UtilsCommon {

  public static String checkFieldsIsJoined(final String originalJavaField,
    final String originalMongoField, final Class<?> childClass,
    final String nameOfChildRelation) {
    if (Arrays.stream(childClass.getDeclaredFields())
      .anyMatch(
        tmp -> originalJavaField.trim().toUpperCase().equals(tmp.getName().toUpperCase()))) {
      return !StringUtils.isBlank(nameOfChildRelation.trim())
        ? nameOfChildRelation.concat(".").concat(originalMongoField)
        : originalMongoField;
    } else {
      return originalMongoField;
    }
  }

  public static List<String> listOfAllFieldsInClasses(final List<Class> listClasses) {
    List<String> listOfAllFields = new ArrayList<String>();
    for (Class clss : listClasses) {
      listOfAllFields = Stream
        .concat(listOfAllFields.stream(), Arrays.asList(clss.getDeclaredFields()).stream()
          .map(fieldOfClass -> fieldOfClass.getName()))
        .collect(Collectors.toList());
    }
    return listOfAllFields;
  }

  public static List<ProblemParamType> checkIfAllFiltersExist(
    final List<FilterType> listFilters, final List<String> fieldsInAllClasses,
    final List<ProblemParamType> mapNotFound) {
    List<String> listFltr = listFilters.stream()
      .map(fltr -> fltr.getAttribute().trim())
      .collect(Collectors.toList());
    listFltr.forEach(fld -> {
      if (!fieldsInAllClasses.contains(fld)) {
        ProblemParamType ppt = new ProblemParamType();
        ppt.setName(fld);
        ppt.setReason(Constants.MSG_ERROR_FILTER_NOT_EXIST);
        if (!mapNotFound.contains(ppt)) {
          mapNotFound.add(ppt);
        }
      }
    });
    return mapNotFound;
  }

  public static List<ProblemParamType> checkIfAllRestrictionsAreOk(
    final List<RestrictionRootType> listRestrictionsRootTypes,
    final List<String> fieldsInAllClasses,
    final List<ProblemParamType> mapNotFound) {
    // Get all attributes to check if exist in DAO
    List<String> listRestr = listRestrictionsRootTypes.stream()
      .map(lrrt -> lrrt.getAttribute()).collect(Collectors.toList());
    listRestr.forEach(fld -> {
      if (!fieldsInAllClasses.contains(fld)) {
        ProblemParamType ppt = new ProblemParamType();
        ppt.setName(fld);
        ppt.setReason(Constants.MSG_ERROR_RESTRICTION_NOT_EXIST);
        if (!mapNotFound.contains(ppt)) {
          mapNotFound.add(ppt);
        }
      }
    });
    listRestrictionsRootTypes.forEach(restrictionRoot -> {
      String type = restrictionRoot.getType().getValue().toUpperCase();
      String attribute = restrictionRoot.getAttribute();
      List<String> listRestrictionValues = restrictionRoot.getValues().stream()
        .map(restrictionValues -> restrictionValues.getValue()).collect(Collectors.toList());
      listRestrictionValues.forEach(restVal -> {
        if (!Constants.TYPE_OF_STRING.equals(type) && !Constants.TYPE_OF_BOOLEAN.equals(type)
          && (!NumberUtils.isCreatable(restVal))) {
          ProblemParamType ppt = new ProblemParamType();
          ppt.setName(attribute);
          ppt.setReason(Constants.MSG_ERROR_RESTRICTION_MUST_BE_NUMBER);
          if (!mapNotFound.contains(ppt)) {
            mapNotFound.add(ppt);
          }
        }
      });
    });
    return mapNotFound;
  }

  public static void throwInvalidParams(final List<FilterType> listFilters,
    final List<RestrictionRootType> listRestrictionRootTypes,
    final List<String> fieldsInAllClasses) {
    List<ProblemParamType> resultsNotFound = new ArrayList<ProblemParamType>();
    resultsNotFound = (listFilters != null && !listFilters.isEmpty())
      ? checkIfAllFiltersExist(listFilters,
      fieldsInAllClasses, resultsNotFound)
      : resultsNotFound;
    resultsNotFound = (listRestrictionRootTypes != null && !listRestrictionRootTypes.isEmpty())
      ? checkIfAllRestrictionsAreOk(listRestrictionRootTypes,
      fieldsInAllClasses, resultsNotFound)
      : resultsNotFound;
    if (!resultsNotFound.isEmpty()) {
      List<ProblemDetailType> listProblemDetailType = new ArrayList<ProblemDetailType>();
      ProblemDetailType problemDetailType = new ProblemDetailType();
      problemDetailType.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
      problemDetailType.setStatus(Integer.valueOf(HttpStatus.NOT_FOUND.value()));
      problemDetailType.setInvalidParams(resultsNotFound);
      listProblemDetailType.add(problemDetailType);
      ProblemType problemType = new ProblemType();
      problemType.addErrorsItem(problemDetailType);
      throw new BadRequestException(Constants.INSTANCE, null,
        Constants.MSG_ERROR_RESOURCE_NOT_FOUND, problemType);
    }
  }

  public static String formatDateStandardWorld(final String stringDate, final String fieldName) {
    String dateToFormat = stringDate.trim();
    // We check if is a date with format yyyyMMdd, than can be convert to numeric
    if (!NumberUtils.isCreatable(dateToFormat)) {
      // Format is not valid; We throw an exception
      throw new BadRequestException(fieldName, dateToFormat,
        MessageFormat.format(Constants.MSG_ERROR_DATE,
          fieldName, stringDate),
        buildProblemType(Constants.TYPE_URL, null,
          MessageFormat.format(Constants.MSG_ERROR_DATE,
            fieldName, stringDate)));
    }
    if (dateToFormat.length() == Constants.LENGHT_SHORT_DATE) {
      dateToFormat = dateToFormat
        .substring(Constants.START_POSITION_OF_YEAR, Constants.END_POSITION_OF_YEAR)
        .concat(Constants.DATE_SEPARATOR)
        .concat(dateToFormat
          .substring(Constants.START_POSITION_OF_MONTH, Constants.END_POSITION_OF_MONTH)
          .concat(Constants.DATE_SEPARATOR)
          .concat(
            dateToFormat.substring(Constants.START_POSITION_OF_DAY, Constants.END_POSITION_OF_DAY))
          .concat(Constants.SUFIX_DATE_ISO_INSTANT));
    }
    return dateToFormat;
  }

  public static ProblemType buildProblemType(final String type_url, final List<String> params,
    final String message) {
    List<ProblemDetailType> listProblemDetailType = new ArrayList<ProblemDetailType>();
    ProblemDetailType problemDetailType = new ProblemDetailType();
    if (params != null) {
      problemDetailType.setType(
        MessageFormat.format(type_url, params.toArray()));
    }
    problemDetailType.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
    problemDetailType.setStatus(Integer.valueOf(HttpStatus.NOT_FOUND.value()));
    problemDetailType.setInstance(Constants.INSTANCE);
    problemDetailType.setDetail(message);
    listProblemDetailType.add(problemDetailType);
    ProblemType problemType = new ProblemType();
    problemType.addErrorsItem(problemDetailType);
    return problemType;
  }
}

