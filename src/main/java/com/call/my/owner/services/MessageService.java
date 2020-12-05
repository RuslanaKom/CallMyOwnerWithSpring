package com.call.my.owner.services;

import com.call.my.owner.dao.MessageDao;
import com.call.my.owner.dto.MessageDto;
import com.call.my.owner.entities.Message;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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
        message.setNew(true);
        messageDao.save(message);
    }

    public List<MessageDto> getMessagesByUserAndStuff(ObjectId userId, String stuffId, int offset, int size, String direction) {
        PageRequest request = PageRequest.of(offset, size, Sort.by(Sort.Direction.valueOf(direction), "receivedDate"));
        return messageDao.findByUserIdAndStuffId(userId, new ObjectId(stuffId), request)
                .getContent()
                .stream()
                .map(MessageDto::toDto)
                .collect(Collectors.toList());
    }

    public void updateMessagesAsShown(List<String> shownMessagesIds) {
        List<ObjectId> ids = shownMessagesIds.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());
        messageDao.findByIdIn(ids)
                .stream()
                .forEach(message -> {
                    message.setNew(false);
                    messageDao.save(message);
                });
    }

    public Long countMessagesByUserAndStuff(ObjectId userId, ObjectId stuffId) {
        return messageDao.countByUserIdAndStuffId(userId, stuffId);
    }

    public boolean newMessagesExist(ObjectId stuffId, ObjectId userId) {
        return messageDao.existsByStuffIdAndUserIdAndIsNew(stuffId, userId, true);
    }
}
