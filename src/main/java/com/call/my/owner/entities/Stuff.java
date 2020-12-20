package com.call.my.owner.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="stuff")
public class Stuff {

    @Id
    private ObjectId id;
    @Indexed
    private ObjectId userId;
    @Indexed
    private String stuffName;
    private String defaultMessage;

    public Stuff(ObjectId id, ObjectId userId, String stuffName, String defaultMessage) {
        this.id = id;
        this.userId = userId;
        this.stuffName = stuffName;
        this.defaultMessage = defaultMessage;
    }

    public Stuff(String stuffName, String defaultMessage) {
        this.stuffName = stuffName;
        this.defaultMessage = defaultMessage;
    }

    public Stuff() {
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}
