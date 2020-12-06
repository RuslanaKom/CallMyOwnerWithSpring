package com.call.my.owner.dto;

import com.call.my.owner.entities.Message;

import java.time.LocalDateTime;

public class MessageDto {

    private String id;
    private String stuffName;
    private String messageText;
    private LocalDateTime receivedDate;
    private boolean isNew;

    public static final MessageDto toDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId().toHexString());
        messageDto.setStuffName(message.getStuffName());
        messageDto.setMessageText(message.getMessageText());
        messageDto.setReceivedDate(message.getReceivedDate());
        messageDto.setNew(message.isNew());
        return messageDto;
    }

    public String getStuffName() {
        return stuffName;
    }

    public void setStuffName(String stuffName) {
        this.stuffName = stuffName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
