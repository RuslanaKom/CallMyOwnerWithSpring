package com.call.my.owner.dao;

import com.call.my.owner.entities.Stuff;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StuffDao extends MongoRepository<Stuff, ObjectId> {

    Page<Stuff> findByUserId(ObjectId userId, Pageable pageable);

    Page<Stuff> findByUserIdAndStuffNameStartingWith(ObjectId userId, String stuffName, Pageable pageable);

    boolean existsByUserIdAndStuffName(ObjectId userId, String stuffName);

    boolean existsByIdAndUserId(ObjectId stuffId, ObjectId userId);

}
