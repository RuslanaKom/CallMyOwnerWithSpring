package com.call.my.owner.dao;

import com.call.my.owner.entities.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageDao extends MongoRepository<Message, ObjectId> {

    Page<Message> findByUserId(ObjectId userId, Pageable pageable);

    Page<Message> findByUserIdAndStuffId(ObjectId userId, ObjectId stuffId, Pageable pageable);

    Page<Message> findByUserIdAndStuffIdAndMessageTextContaining(ObjectId userId, ObjectId stuffId, String messageText, Pageable pageable);

    void deleteByStuffId(ObjectId stuffId);

    List<Message> findByIdIn(List<ObjectId> ids);

    Long countByUserIdAndStuffId(ObjectId userId, ObjectId stuffId);

    boolean existsByStuffIdAndUserIdAndIsNew(ObjectId stuffId, ObjectId userId, boolean isNew);
}
