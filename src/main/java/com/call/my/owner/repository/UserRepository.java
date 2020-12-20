package com.call.my.owner.repository;

import com.call.my.owner.entities.UserAccount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserAccount, ObjectId> {

    UserAccount findByUsername(String username);

    UserAccount findByDefaultEmail(String email);

    boolean existsByUsername(String username);
}
