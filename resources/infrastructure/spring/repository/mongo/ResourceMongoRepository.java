package com.bme.clp.bck.resources.q.infrastructure.spring.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bme.clp.bck.resources.q.domain.model.mongo.ModelDAO;
import com.bme.clp.bck.resources.q.domain.model.mongo.ModelPKDAO;

public interface ResourceMongoRepository extends MongoRepository<ModelDAO, ModelPKDAO> {
}