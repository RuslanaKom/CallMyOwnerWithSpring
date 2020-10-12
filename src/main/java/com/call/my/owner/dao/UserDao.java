package com.call.my.owner.dao;

import com.call.my.owner.entities.UserAccount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<UserAccount, ObjectId> {

    UserAccount findByUsername(String username);

    UserAccount findByDefaultEmail(String email);
}
