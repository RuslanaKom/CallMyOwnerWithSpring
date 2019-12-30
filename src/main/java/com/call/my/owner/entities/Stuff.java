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

    public Stuff(ObjectId id, ObjectId userId, String stuffName, String contactEmail, String defaultMessage) {
        this.id = id;
        this.userId = userId;
        this.stuffName = stuffName;
        this.contactEmail = contactEmail;
        this.defaultMessage = defaultMessage;
    }

    public Stuff(String stuffName, String contactEmail, String defaultMessage) {
        this.stuffName = stuffName;
        this.contactEmail = contactEmail;
        this.defaultMessage = defaultMessage;
    }

    public Stuff() {
    }

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
