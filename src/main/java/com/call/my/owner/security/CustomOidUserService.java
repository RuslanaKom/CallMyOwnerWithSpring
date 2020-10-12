package com.call.my.owner.security;

import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.services.UserAccountService;
import org.bson.types.ObjectId;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class CustomOidUserService extends OidcUserService {

    private final UserAccountService userAccountService;
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = new DefaultOAuth2UserService();

    public CustomOidUserService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        Assert.notNull(userRequest, "userRequest cannot be null");
        OAuth2User oauth2User = this.oauth2UserService.loadUser(userRequest);
        String email = (String) oauth2User.getAttributes()
                .get("email");
        UserAccount userAccount = userAccountService.loadUserByEmail(email);
        if (userAccount == null) {
            userAccount = userAccountService.createUserAccount(new UserAccount(email, new ObjectId().toHexString(), email), true);
        }
        DefaultOidcUser user = new DefaultOidcUser(userAccount.getAuthorities(), userRequest.getIdToken());
        return user;
    }

}
