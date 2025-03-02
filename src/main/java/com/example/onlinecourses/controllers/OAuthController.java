package com.example.onlinecourses.controllers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuthController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/callback")
    public String callback(@RegisteredOAuth2AuthorizedClient OAuth2AuthenticationToken authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        String providerId = user.getAttribute("sub"); // Adjust based on your provider's user info
        return "OAuth login successful. Provider ID: " + providerId;
    }

}
