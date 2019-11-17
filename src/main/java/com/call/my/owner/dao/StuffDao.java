package com.call.my.owner.dao;

import com.call.my.owner.entities.Stuff;
import com.call.my.owner.entities.UserAccount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StuffDao extends MongoRepository<Stuff, String> {

    @Query("{userId: ?0 }")
    List<Stuff> findByUserId(ObjectId userId);

    @Query("{userId: ?0 }, {stuffName:1}")
    List<Stuff> findStuffNamesByUserId(ObjectId userId);
}
