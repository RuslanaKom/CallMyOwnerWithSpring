package com.call.my.owner.dto;

import com.call.my.owner.entities.Message;

import java.time.LocalDateTime;

public class MessageDto {

    private String messageText;
    private LocalDateTime receivedDate;

    public static final MessageDto toDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessageText(message.getMessageText());
        messageDto.setReceivedDate(message.getReceivedDate());
        return messageDto;
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
}
