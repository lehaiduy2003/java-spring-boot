package com.example.onlinecourses.configs.impls;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OAuth2UserImpl implements OAuth2User {
    private final OAuth2User oauth2User;
    private final Map<String, Object> attributes;

    public OAuth2UserImpl(OAuth2User oauth2User, String role, String provider) {
        this.oauth2User = oauth2User;
        this.attributes = new HashMap<>(oauth2User.getAttributes()); // Copy attributes from OAuth2User
        this.attributes.put("role", role); // add role into attributes
        this.attributes.put("provider", provider); // add provider into attributes
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes; // return extended attributes
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String getRole() {
        return (String) attributes.get("role");
    }

    public String getProvider() {
        return (String) attributes.get("provider");
    }
}
