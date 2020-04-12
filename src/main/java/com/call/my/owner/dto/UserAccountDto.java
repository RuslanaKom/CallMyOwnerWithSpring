package com.call.my.owner.dto;

import com.call.my.owner.entities.UserAccount;

public class UserAccountDto {
    private String username;
    private String password;
    private String defaultEmail;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultEmail() {
        return defaultEmail;
    }

    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = defaultEmail;
    }

    public UserAccount toUserAccount(){
        return new UserAccount(this.username, this.password, this.defaultEmail);
    }
}
