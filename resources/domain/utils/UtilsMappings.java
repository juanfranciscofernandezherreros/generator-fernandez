package com.bme.clp.bck.resources.q.domain.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bme.clp.bck.resources.q.web.api.model.FilterType;
import com.bme.clp.bck.resources.q.web.api.model.RestrictionRootType;

public class UtilsMappings {

  public static List<FilterType> mappingFilter(final List<FilterType> listFilters) {
    List<FilterType> result = new ArrayList<FilterType>();
    listFilters.forEach(filter -> {
      //Add specific equivalence in case of field change.
      result.add(filter);
    });
    return result;
  }

  public static List<RestrictionRootType> mappingRestrictions(
    final List<RestrictionRootType> listRestrictionsRootTypes) {
    List<RestrictionRootType> result = new ArrayList<RestrictionRootType>();
    listRestrictionsRootTypes.forEach(restrictionRootType -> {
      //Add specific equivalence in case of field change.
      result.add(restrictionRootType);
    });
    return result;
  }

  public static String mappingSorting(final String sorting) {
    if (sorting != null) {
      String[] sortingSplit = sorting.split(",");
      List<String> result = new ArrayList<String>();
      Arrays.asList(sortingSplit).forEach(sort -> {
        //String order = sort.startsWith("-") ? "-" : "+";
        //Add specific equivalence in case of field change.
        result.add(sort);
      });
      return String.join(",", result);
    } else {
      return "";
    }
  }

}