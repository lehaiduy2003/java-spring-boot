package com.example.onlinecourses.services.interfaces;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface IOAuth2Service {
    AuthResponseDTO continueWithOAuth(OAuth2AuthenticationToken oAuthToken);
}
