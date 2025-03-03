package com.example.onlinecourses.services.interfaces;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Interface for extracting OAuth2 user details
 * Each OAuth2 provider has different attributes for the user
 */
public interface IExtractOAuthUserAttributes {
    String extractEmail(OAuth2User oAuth2User);
    String extractName(OAuth2User oAuth2User);
    String extractPicture(OAuth2User oAuth2User);
}
