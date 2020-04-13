package com.call.my.owner.dto;

import com.call.my.owner.entities.Stuff;

public class StuffDto {

    private String id;
    private String userId;
    private String stuffName;
    private String contactEmail;
    private String defaultMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public StuffDto() {
    }

    public StuffDto(Stuff stuff) {
        this.id = stuff.getId()
                .toHexString();
        this.userId = stuff.getUserId()
                .toHexString();
        this.stuffName = stuff.getStuffName();
        this.contactEmail = stuff.getContactEmail();
        this.defaultMessage = stuff.getDefaultMessage();
    }

    public String getStuffName() {
        return stuffName;
    }

    public void setStuffName(String stuffName) {
        this.stuffName = stuffName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

}
