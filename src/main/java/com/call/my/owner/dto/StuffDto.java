package com.call.my.owner.dto;

import com.call.my.owner.entities.Stuff;
import com.call.my.owner.utils.CapitalLetterFormatUtils;
import org.bson.types.ObjectId;

public class StuffDto {

    private String id;
    private String userId;
    private String stuffName;
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


    public static StuffDto toDto(Stuff stuff) {
        StuffDto stuffDto = new StuffDto();
        stuffDto.setId(stuff.getId()
                .toHexString());
        stuffDto.setUserId(stuff.getUserId()
                .toHexString());
        stuffDto.setStuffName(stuff.getStuffName());
        stuffDto.setDefaultMessage(stuff.getDefaultMessage());
        return stuffDto;
    }

    public static Stuff fromDto(StuffDto stuffDto) {
        Stuff stuff = new Stuff();
        if (stuffDto.getId() != null) {
            stuff.setId(new ObjectId(stuffDto.getId()));
        }
        if (stuffDto.getUserId() != null) {
            stuff.setUserId(new ObjectId(stuffDto.getUserId()));
        }
        stuff.setStuffName(CapitalLetterFormatUtils.formatText(stuffDto.getStuffName()));
        stuff.setDefaultMessage(stuffDto.getDefaultMessage());
        return stuff;
    }

    public String getStuffName() {
        return stuffName;
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
