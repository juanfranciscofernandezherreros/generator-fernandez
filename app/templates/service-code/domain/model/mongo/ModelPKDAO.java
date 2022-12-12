package com.bme.clp.bck.<%=resourceDot%>.q.domain.model.mongo;

import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ModelPKDAO {

  private static final int ORDER_1 = 1;

  @Field(name = "attributeId")
  private String attributeId;

  public ModelPKDAO(final String attributeId) {
    super();
    this.attributeId = attributeId;
  }
}
