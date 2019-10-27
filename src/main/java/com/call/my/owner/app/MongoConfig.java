//package com.call.my.owner.app;
//
//import com.mongodb.MongoClient;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
//@EnableMongoRepositories(basePackages = "com.call.my.owner.dao")
//public class MongoConfig extends AbstractMongoConfiguration {
//
//    private final static String MONGO_DB_NAME = "MongoDatabaseOne";
//    private final static String MONGO_DB_HOST = "localhost";
//    private final static int MONGO_DB_PORT = 27017;
//
//
//    @Override
//    public MongoClient mongoClient() {
//        return new MongoClient(MONGO_DB_HOST, MONGO_DB_PORT);
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return MONGO_DB_NAME;
//    }
//}
