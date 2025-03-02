package com.example.onlinecourses.controllers;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.dtos.responses.ApiResponse;
import com.example.onlinecourses.services.interfaces.IOAuth2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;


@RestController
public class OAuthController {

    private final IOAuth2Service oAuth2ClientService;
    private static final String[] supportedProviders = {"google"}; // Add more providers here

    public OAuthController(IOAuth2Service oAuth2ClientService) {
        this.oAuth2ClientService = oAuth2ClientService;
    }

    @GetMapping("/oauth2/authorization/{provider}")
    public String initiateOAuth2Authorization(@PathVariable String provider) {
        if (!Arrays.asList(supportedProviders).contains(provider)) {
            throw new IllegalArgumentException("Provider not supported");
        }
        // redirect to OAuth2 provider
        return "redirect:/oauth2/authorization/" + provider;
    }

    @GetMapping("/api/v1/oauth2/callback")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> handleCallback(OAuth2AuthenticationToken oAuthToken) {
        AuthResponseDTO authDTO = oAuth2ClientService.continueWithOAuth(oAuthToken);
        return ResponseEntity.ok(new ApiResponse<>(true, "User logged in successfully", authDTO));
    }

}
