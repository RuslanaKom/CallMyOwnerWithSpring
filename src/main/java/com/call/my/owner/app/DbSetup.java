package com.call.my.owner.app;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;


public class DbSetup {
    private final static String USER_COLLECTION = "users";
    private static final String STUFF_COLLECTION = "stuff";
    MongoClient mongo = MongoClients.create();
    MongoDatabase database = mongo.getDatabase("MongoDatabaseOne");

    public void createUserCollection(){
        database.createCollection(USER_COLLECTION);
        IndexOptions opts = new IndexOptions();
        opts.unique(true);
        database.getCollection(USER_COLLECTION).createIndex(new BasicDBObject("username",1), opts);
    }

    public void createPropertyCollection(){
        database.createCollection(STUFF_COLLECTION);
    }


}
