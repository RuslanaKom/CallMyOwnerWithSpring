package com.call.my.owner.security;

import com.call.my.owner.entities.UserAccount;
import com.call.my.owner.services.UserAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final String baseFrontUrl;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAccountService userAccountService;

    public CustomAuthenticationSuccessHandler(@Value("${app.front.url}") String baseBackOfficeUrl, JwtTokenProvider jwtTokenProvider,
                                              UserAccountService userAccountService) {
        this.baseFrontUrl = baseBackOfficeUrl;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAccountService = userAccountService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OidcUser principal = (OidcUser) authentication.getPrincipal();
        UserAccount userAccount = userAccountService.loadUserByEmail(principal.getEmail());
        String token = jwtTokenProvider.generateToken(userAccount.getUsername());
        getRedirectStrategy().sendRedirect(request, response, baseFrontUrl + "/auth/" + token);
    }

}
