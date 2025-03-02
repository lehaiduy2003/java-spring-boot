package com.example.onlinecourses.configs;

import com.example.onlinecourses.components.OAuthSuccessHandler;
import com.example.onlinecourses.repositories.OAuth2ProviderRepository;
import com.example.onlinecourses.repositories.UsersRepository;
import com.example.onlinecourses.services.abstracts.BaseOAuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.function.Consumer;

@Configuration
@Getter
@RequiredArgsConstructor
public class OAuth2Config {

    private final UsersRepository usersRepository;
    private final OAuth2ProviderRepository oAuth2ProviderRepository;
    private final OAuthSuccessHandler oAuthSuccessHandler;

    @Bean
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver resolver =
            new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        resolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());

        return resolver;
    }

    // This method customizes the OAuth2AuthorizationRequest by adding the prompt parameter
    // So the user can select an account to sign in with every time
    private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
        return customizer -> customizer
            .additionalParameters(params -> {
                params.put("prompt", "select_account"); // Force account selection
            });
    }

    // OAuth2UserService bean for handling OAuth2 user details
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new BaseOAuthService(
            usersRepository, oAuth2ProviderRepository
        );
    }
}
