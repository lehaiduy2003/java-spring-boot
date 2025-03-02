package com.example.onlinecourses.services;

import com.example.onlinecourses.models.OauthProvider;
import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.repositories.OAuth2ProviderRepository;
import com.example.onlinecourses.repositories.RolesRepository;
import com.example.onlinecourses.repositories.UsersRepository;
import com.example.onlinecourses.specifications.UserSpecification;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;
    private final OAuth2ProviderRepository oAuth2ProviderRepository;
    private final RolesRepository roleRepository;

    public OAuth2UserService(UsersRepository usersRepository, OAuth2ProviderRepository oAuth2ProviderRepository, RolesRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.oAuth2ProviderRepository = oAuth2ProviderRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Extract user details from the OAuth2User object
        String providerId = oauth2User.getAttribute("sub"); // Unique provider ID for the user
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String avatar = oauth2User.getAttribute("picture");

        // Extract provider name from OAuth2UserRequest
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // Get the selected role from the OAuth2User attributes
        String roleName = (String) oauth2User.getAttributes().get("role");
        if (roleName == null) {
            throw new RuntimeException("Role not found in OAuth2User attributes");
        }

        // Find or create the role
        Role role = roleRepository.findByName(roleName.toUpperCase())
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        // Check if the user already exists in the database
        User user = usersRepository.findOne(UserSpecification.hasEmail(email))
            .orElseGet(() -> {
                // Create a new user if not found
                User newUser = User.builder()
                    .email(email)
                    .fullname(name)
                    .avatar(avatar)
                    .username(email) // Use email as username for simplicity
                    .build();
                newUser.addRole(role);
                return usersRepository.save(newUser);
            });

        // Check if the OAuth provider already exists for this user
        OauthProvider oauthProvider = oAuth2ProviderRepository.findByProviderId(providerId)
            .orElseGet(() -> {
                // Create a new OAuth provider if not found
                OauthProvider newProvider = OauthProvider.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .user(user)
                    .build();
                return oAuth2ProviderRepository.save(newProvider);
            });

        // Add the OAuth provider to the user if not already added
        if (!user.getOauthProviders().contains(oauthProvider)) {
            user.addOauthProvider(oauthProvider);
            usersRepository.save(user);
        }

        return oauth2User;
    }
}
