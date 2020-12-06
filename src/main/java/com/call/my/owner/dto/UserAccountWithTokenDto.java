package com.call.my.owner.dto;

import com.call.my.owner.security.JwtAuthenticationResponse;

public class UserAccountWithTokenDto {

    private JwtAuthenticationResponse accessToken;
    private UserAccountDto userAccountDto;

    public JwtAuthenticationResponse getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(JwtAuthenticationResponse accessToken) {
        this.accessToken = accessToken;
    }

    public UserAccountDto getUserAccountDto() {
        return userAccountDto;
    }

    public void setUserAccountDto(UserAccountDto userAccountDto) {
        this.userAccountDto = userAccountDto;
    }
}
