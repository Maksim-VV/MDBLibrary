package com.vasiliska.MDBLibrary;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

@SpringBootApplication
@EntityScan(basePackages = {"com.vasiliska.MDBLibrary.domain"})
@ComponentScan(basePackages = {"com.vasiliska.MDBLibrary"})
//@ComponentScan(basePackages = {"com.vasiliska.MDBLibrary.repository"})
public class MdbLibraryApplication {

    private static final String MONGO_DB_URL = "localhost";
    private static final String MONGO_DB_NAME = "embeded";
    private static final int PORT = 12345;

    @Bean
    @DependsOn("mongoClient")
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, MONGO_DB_NAME);
    }

    @Bean(destroyMethod = "close")
    @DependsOn("embeddedMongoTest")
    public MongoClient mongoClient() throws IOException {
        return new MongoClient(MONGO_DB_URL, PORT);
    }

    @Bean(destroyMethod = "stop", initMethod = "start")
    public EmbeddedMongoTest embeddedMongoTest() {
        return new EmbeddedMongoTest(MONGO_DB_URL, PORT);
    }

}
