package com.call.my.owner.entities;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="stuff")
public class Stuff {

    @Id
    private ObjectId id;
    private ObjectId userId;
    private String stuffName;
    private String contactEmail;
    private String defaultMessage;

    public ObjectId getId() {
        return id;
    }

    public String getStuffName() {
        return stuffName;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
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
