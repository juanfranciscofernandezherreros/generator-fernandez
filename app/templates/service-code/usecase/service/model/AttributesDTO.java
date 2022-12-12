package com.bme.clp.bck.<%=resourceDot%>.q.usecase.service.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AttributesDTO {

  private String attribute;
  private String attributeId;

  public String getIdResponse() {
    return attributeId;
  }

}