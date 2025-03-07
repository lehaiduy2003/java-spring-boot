package com.example.onlinecourses.configs;

import com.example.onlinecourses.backgroundJobs.interfaces.ITokenBlacklistService;
import com.example.onlinecourses.filters.CookieFilter;
import com.example.onlinecourses.filters.JwtFilter;
import com.example.onlinecourses.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final OAuth2Config oAuth2Config;
    private final ITokenBlacklistService tokenBlacklistService;
    private final IUserService userService;

    private static final String[] WHITE_LIST = {
            "/", // Allow access to the home page
            "/v3/api-docs/**", // Allow access to API docs
            "/swagger-ui/**", // Allow access to Swagger UI
            "/css/**", // Allow access to CSS files
            "/js/**", // Allow access to JS files
            "/oauth2/authorization/**",
            "/api/v1/auth/**",
            "/api/v1/oauth2/**",
    };

    private static final String[] ALLOWED_READ_ENDPOINTS = {
        "/api/v1/users/**",
        "/api/v1/courses/**",
        "/api/v1/lessons/**",
        "/api/v1/sections/**",
        "/api/v1/discussions/**",
        "/api/v1/subjects/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
            // Authorize Requests
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(WHITE_LIST).permitAll() // Allow access endpoints
                .requestMatchers(HttpMethod.GET, ALLOWED_READ_ENDPOINTS).permitAll() // Allow GET requests for read endpoints
                .anyRequest().authenticated() // All other endpoints require authentication
            );
        // OAuth2 Login
        http
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/index").permitAll()
                .authorizationEndpoint(authorization -> authorization
                    .authorizationRequestResolver(oAuth2Config.authorizationRequestResolver(null)) // Use the resolver from OAuth2Config
                )
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oAuth2Config.oauth2UserService()) // Use the OAuth2UserService from OAuth2Config to handle user details
                )
                .successHandler(oAuth2Config.getOAuthSuccessHandler()) // Use the success handler from OAuth2Config
            );
        // Basic Authentication
        http
            .httpBasic(withDefaults()) // Enable basic authentication
            .formLogin(login -> login
                .loginPage("/index").permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/").permitAll()
            );
        //  Custom Filter
        http
            .addFilterBefore(new JwtFilter(tokenBlacklistService, userService), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new CookieFilter(tokenBlacklistService, userService), JwtFilter.class);


        return http.build();
    }

}