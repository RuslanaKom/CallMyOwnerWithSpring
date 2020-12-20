package com.call.my.owner.repository;

import com.call.my.owner.entities.UnconfirmedEmail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnconfirmedEmailRepository extends MongoRepository<UnconfirmedEmail, ObjectId> {

}
