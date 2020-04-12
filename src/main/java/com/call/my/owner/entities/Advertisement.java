package com.call.my.owner.entities;

import com.call.my.owner.enums.AdCategory;
import com.call.my.owner.enums.AdType;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="advertisement")
public class Advertisement {

    @Id
    private ObjectId id;
    private String text;
    private AdType type;
    private AdCategory category;
    private String contactInfo;
    private String region;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }

    public AdCategory getCategory() {
        return category;
    }

    public void setCategory(AdCategory category) {
        this.category = category;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
