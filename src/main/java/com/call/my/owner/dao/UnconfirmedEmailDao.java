package com.call.my.owner.dao;

import com.call.my.owner.entities.UnconfirmedEmail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnconfirmedEmailDao extends MongoRepository<UnconfirmedEmail, ObjectId> {

}
