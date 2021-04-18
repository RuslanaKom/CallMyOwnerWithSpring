package com.call.my.owner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableMongoRepositories(basePackages = {"com.call.my.owner.repository"})
public class CallMyOwnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallMyOwnerApplication.class, args);
    }

}
