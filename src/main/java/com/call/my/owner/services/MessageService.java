package com.call.my.owner.services;

import com.call.my.owner.repository.MessageRepository;
import com.call.my.owner.dto.MessageDto;
import com.call.my.owner.entities.Message;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(String messageText, ObjectId stuffId, String stuffName, ObjectId userId) {
        Message message = new Message();
        message.setMessageText(messageText);
        message.setStuffId(stuffId);
        message.setUserId(userId);
        message.setStuffName(stuffName);
        message.setReceivedDate(LocalDateTime.now());
        message.setNew(true);
        messageRepository.save(message);
    }

    public Page<MessageDto> getMessagesByUserAndStuff(ObjectId userId, String stuffId, int offset, int size, String direction) {
        PageRequest request = PageRequest.of(offset, size, Sort.by(Sort.Direction.valueOf(direction), "receivedDate"));
        return messageRepository.findByUserIdAndStuffId(userId, new ObjectId(stuffId), request)
                .map(MessageDto::toDto);
    }

    public void updateMessagesAsShown(List<String> shownMessagesIds) {
        List<ObjectId> ids = shownMessagesIds.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());
        messageRepository.findByIdIn(ids)
                .forEach(message -> {
                    message.setNew(false);
                    messageRepository.save(message);
                });
    }

    public boolean newMessagesExist(ObjectId stuffId, ObjectId userId) {
        return messageRepository.existsByStuffIdAndUserIdAndIsNew(stuffId, userId, true);
    }

    public Page<MessageDto> getMessagesByUserAndStuffAndText(ObjectId userId, String stuffId, int offset, int size, String direction, String messageText) {
        PageRequest request = PageRequest.of(offset, size, Sort.by(Sort.Direction.valueOf(direction), "receivedDate"));
        return messageRepository.findByUserIdAndStuffIdAndMessageTextContaining(userId, new ObjectId(stuffId), messageText, request)
                .map(MessageDto::toDto);
    }

    public Page<MessageDto> getMessagesByUser(ObjectId userId, int offset, int size, String direction) {
            PageRequest request = PageRequest.of(offset, size, Sort.by(Sort.Direction.valueOf(direction), "receivedDate"));
            return messageRepository.findByUserId(userId, request)
                    .map(MessageDto::toDto);
        }
}
