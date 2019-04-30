package com.vasiliska.MDBLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.vasiliska.MDBLibrary")
public class MdbLibraryApplication {

    public static void main(String[] args) {
        
        SpringApplication.run(MdbLibraryApplication.class, args);

    }

}
