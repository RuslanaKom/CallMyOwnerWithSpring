package com.call.my.owner.dao;

import com.call.my.owner.entities.Message;
import com.call.my.owner.entities.Stuff;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageDao extends MongoRepository<Message, ObjectId> {

    Page<Message> findByUserIdAndStuffId(ObjectId userId, ObjectId stuffId, Pageable pageable);

    void deleteByStuffId(ObjectId stuffId);

    List<Message> findByIdIn(List<ObjectId> ids);

    Long countByUserId(ObjectId userId);

    boolean existsByStuffIdAndUserIdAndIsNew(ObjectId stuffId, ObjectId userId, boolean isNew);
}
