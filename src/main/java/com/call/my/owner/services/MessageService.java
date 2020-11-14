package com.call.my.owner.services;

import com.call.my.owner.dao.MessageDao;
import com.call.my.owner.dto.MessageDto;
import com.call.my.owner.entities.Message;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final MessageDao messageDao;

    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void saveMessage(String messageText, ObjectId stuffId, String stuffName, ObjectId userId) {
        Message message = new Message();
        message.setMessageText(messageText);
        message.setStuffId(stuffId);
        message.setUserId(userId);
        message.setStuffName(stuffName);
        message.setReceivedDate(LocalDateTime.now());
        messageDao.save(message);
    }

public List<MessageDto> getMessagesByUserAndStuff(ObjectId userId, String stuffId){
        return messageDao.findByUserIdAndStuffId(userId, new ObjectId(stuffId))
                .stream()
                .map(MessageDto::toDto)
                .collect(Collectors.toList());
}
}
