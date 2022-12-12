package com.bme.clp.bck.resources.q.infrastructure.spring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("use.mongo")
public class MongoConfig {
}