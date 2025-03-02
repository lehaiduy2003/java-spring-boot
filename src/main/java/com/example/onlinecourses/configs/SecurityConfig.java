package com.example.onlinecourses.configs;

import com.example.onlinecourses.filters.CookieFilter;
import com.example.onlinecourses.filters.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2Config oAuth2Config;

    public SecurityConfig(OAuth2Config oAuth2Config) {
        this.oAuth2Config = oAuth2Config;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/static/**",
                    "/api/v1/auth/sign-in", "/api/v1/auth/sign-up","/api/v1/auth/sign-out",
                    "/oauth2/authorization/**").permitAll() // Allow access endpoints
                .anyRequest().authenticated() // All other endpoints require authentication
            )
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization
                    .authorizationRequestResolver(oAuth2Config.authorizationRequestResolver(null)) // Use the resolver from OAuth2Config
                )
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oAuth2Config.oauth2UserService()) // Use the OAuth2UserService from OAuth2Config
                )
                .successHandler(oAuth2Config.authenticationSuccessHandler()) // Handle role assignment
                .defaultSuccessUrl("/home", true)
            )
            .httpBasic(withDefaults()) // Enable basic authentication
            .logout(logout -> logout
                .logoutSuccessUrl("/").permitAll()
            )
            .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new CookieFilter(), JwtFilter.class);
        return http.build();
    }

}