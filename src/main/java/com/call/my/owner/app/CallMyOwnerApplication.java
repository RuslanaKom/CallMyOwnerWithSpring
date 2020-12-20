package com.call.my.owner.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableMongoRepositories(basePackages= {"com.call.my.owner.repository"})
@ComponentScan(basePackages= {"com.call.my.owner.app","com.call.my.owner.controllers", "com.call.my.owner.repository", "com.call.my.owner.entities", "com.call.my.owner.security", "com.call.my.owner.services"})
		public class CallMyOwnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallMyOwnerApplication.class, args);
	}

}
