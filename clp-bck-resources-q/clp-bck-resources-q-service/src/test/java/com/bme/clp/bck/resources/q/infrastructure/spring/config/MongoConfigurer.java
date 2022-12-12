package com.bme.clp.bck.resources.q.infrastructure.spring.config;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.util.SocketUtils;

import com.bme.clp.bck.resources.q.infrastructure.spring.repository.mongo.ResourceMongoRepository;
import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.distribution.Version;

@Configuration
public class MongoConfigurer {
    public static final String CONNECTION_STRING = "mongodb://%s:%d";

    public static final String DATABASE = "db_" + UUID.randomUUID();

    public static final String HOST = "localhost";

    public static final String BINARY_URI = "https://artifactory.six-group.net"
              + "/artifactory/opensource-generic-release-local/mongodb/3.5.5/";

    public static final Integer PORT = SocketUtils.findAvailableTcpPort();

    public static final Version VERSION = Version.V3_5_5;

    public static final String COLLECTION = "DCLEAR.MUR.KS.SOMETHING";

    @Bean
    @Qualifier("MongoTemplate")
    public MongoTemplate getMongoTemplate() {
        return new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, HOST, PORT)),
                DATABASE);
      }

    @Bean
    public MongoDatabaseFactory factory() {
        return new SimpleMongoClientDatabaseFactory("mongodb://"
                + MongoConfigurer.HOST + ":"
                + MongoConfigurer.PORT + "/"
                + MongoConfigurer.DATABASE);
    }

    @Bean
    public MongoRepositoryFactoryBean mongoFactoryRepositoryBean(final MongoTemplate template) {
        MongoRepositoryFactoryBean mongoDbFactoryBean = new MongoRepositoryFactoryBean(ResourceMongoRepository.class);
        mongoDbFactoryBean.setMongoOperations(template);

        return mongoDbFactoryBean;
    }
}
