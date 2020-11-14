package com.call.my.owner.dao;

import com.call.my.owner.entities.Stuff;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StuffDao extends MongoRepository<Stuff, ObjectId> {

    List<Stuff> findByUserId(ObjectId userId);

    boolean existsByUserIdAndStuffName(ObjectId userId, String stuffName);

    boolean existsByIdAndUserId(ObjectId stuffId, ObjectId userId);
}
