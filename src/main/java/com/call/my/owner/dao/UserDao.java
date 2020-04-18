package com.call.my.owner.dao;

import com.call.my.owner.entities.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserDao extends MongoRepository<UserAccount, String> {

    @Query("{username: ?0 }")
    List<UserAccount> findByUsername(String username);

}
