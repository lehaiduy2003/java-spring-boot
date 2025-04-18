package com.example.onlinecourses.configs.impls;

import com.example.onlinecourses.models.OauthProvider;
import com.example.onlinecourses.models.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String email;
    private final String fullname;
    private final String phoneNumber;
    private final String username;
    private final String password;
    private final boolean isActive;
    private final Set<OauthProvider> oauthProviders;
    private final Set<Role> roles;

    /**
     * Get the authority roles granted to the user
     * @return a role collection of granted authorities for the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roles == null) return new HashSet<>(); // return empty set if roles is null
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .toList();
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
