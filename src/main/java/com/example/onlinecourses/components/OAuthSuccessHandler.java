package com.example.onlinecourses.components;
import com.example.onlinecourses.utils.SecurityContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class for handling OAuth2 success authentication
 * This class will be called when user successfully authenticated using OAuth2 and set the security context
 */
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String provider = oauthToken.getAuthorizedClientRegistrationId();

        OAuth2AuthenticationToken newAuthentication = new OAuth2AuthenticationToken(
            oAuth2User,
            oAuth2User.getAuthorities(),
            provider
        );
        SecurityContextUtil.setSecurityContext(newAuthentication);
        getRedirectStrategy().sendRedirect(request, response, "/api/v1/oauth2/callback");
    }
}
