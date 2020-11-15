package com.call.my.owner.dao;

import com.call.my.owner.entities.Message;
import com.call.my.owner.entities.Stuff;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageDao extends MongoRepository<Message, ObjectId> {

    List<Message> findByUserId(ObjectId userId);

    Page<Message> findByUserIdAndStuffId(ObjectId userId, ObjectId stuffId, Pageable pageable);

    void deleteByStuffId(ObjectId stuffId);
}
