package com.bme.clp.bck.fx.data.q.domain.model.mongo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
@Document(collection = "<%=mongoCollection%>")
public class FxDataDAO {
  @MongoId
  private FxDataPKDAO id;

  @Field(name = "fxRate")
  private Double fxRate;

}