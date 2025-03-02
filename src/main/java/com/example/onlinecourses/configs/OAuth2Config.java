package com.example.onlinecourses.configs;

import com.example.onlinecourses.repositories.OAuth2ProviderRepository;
import com.example.onlinecourses.repositories.RolesRepository;
import com.example.onlinecourses.repositories.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.function.Consumer;

@Configuration
public class OAuth2Config {

    private final UsersRepository usersRepository;
    private final RolesRepository roleRepository;
    private final OAuth2ProviderRepository oAuth2ProviderRepository;

    public OAuth2Config(UsersRepository usersRepository, RolesRepository roleRepository, OAuth2ProviderRepository oAuth2ProviderRepository) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.oAuth2ProviderRepository = oAuth2ProviderRepository;
    }

    @Bean
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver resolver =
            new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        resolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());

        return resolver;
    }

    private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
        return customizer -> customizer
            .additionalParameters(params -> {
                params.put("prompt", "select_account"); // Force account selection
            });
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new com.example.onlinecourses.services.OAuth2UserService(
            usersRepository, oAuth2ProviderRepository, roleRepository
        );
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Get the selected role from the request
            String role = request.getParameter("role");

            // Set the role in the authentication object
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2User oauth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();
                oauth2User.getAttributes().put("role", role); // Add role to OAuth2User attributes
            }

            // Redirect to the home page or another endpoint
            response.sendRedirect("/home");
        };
    }
}
