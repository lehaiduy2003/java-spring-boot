package com.example.onlinecourses.services.abstracts;
import com.example.onlinecourses.models.OauthProvider;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.repositories.OAuth2ProviderRepository;
import com.example.onlinecourses.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseOAuthService extends DefaultOAuth2UserService {
    private final UsersRepository usersRepository;
    private final OAuth2ProviderRepository oAuth2ProviderRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    /**
     * Retrieve the token from the OAuth2AuthorizedClientService
     * @param provider The provider of the OAuth2 user (Google, Facebook, etc.)
     * @param openId The openId of the user
     * @return The token value
     */
    protected String retrieveToken(String provider, String openId) {
        return authorizedClientService.loadAuthorizedClient(provider, openId).getAccessToken().getTokenValue();
    }

    /**
     * Retrieve the openId from the OAuth2User object
     * @param oAuth2User The OAuth2User object
     * @return The openId of the user
     */
    protected String retrieveOpenId(OAuth2User oAuth2User) {
        return oAuth2User.getName();
    }

    protected User checkUser(String email, String name, String picture) {
        return usersRepository.findUserByEmail(email).orElseGet(() -> createUser(email, name, picture));
    }

    // No need to add role here, the role update logic will handle when the first time user sign in
    private User createUser(String email, String name, String picture) {
        User user = User.builder()
            .email(email)
            .fullname(name)
            .avatar(picture)
            .build();
        return usersRepository.save(user);
    }

    /**
     * Check if the OAuth provider already exists for this user or create a new one
     * @param openId The openId of the user
     * @param provider The provider of the OAuth2 user (Google, Facebook, etc.)
     * @param email The email of the user
     * @param user The user object
     * @return The OAuth provider object
     */
    protected OauthProvider checkProvider(String openId, String provider, String email, User user) {
        Optional<OauthProvider> oAuthProviderOptional = oAuth2ProviderRepository.findOauthProviderByOpenId(openId);
        if(oAuthProviderOptional.isPresent()) {
            OauthProvider oauthProvider = oAuthProviderOptional.get();
            // Update the token if it has changed
            String newToken = retrieveToken(provider, openId);
            oauthProvider.setToken(newToken);
            return oAuth2ProviderRepository.save(oauthProvider);
        }
        else {
            // Create a new OAuth provider object
            return createProvider(openId, provider, email, user);
        }
    }

    /**
     * Create a new OAuth provider object and save it to the database
     * @param openId The openId of the user
     * @param provider The provider of the OAuth2 user (Google, Facebook, etc.)
     * @param email The email of the user
     * @param user The user object
     * @return new OAuth provider object
     */
    private OauthProvider createProvider(String openId, String provider, String email, User user) {
        String token = retrieveToken(provider, openId); // Retrieve the token from the OAuth2AuthorizedClientService
        OauthProvider oauthProvider = OauthProvider.builder()
            .openId(openId)
            .provider(provider)
            .linkedEmail(email)
            .token(token)
            .user(user)
            .build();
        return oAuth2ProviderRepository.save(oauthProvider);
    }

    /**
     * Link the OAuth provider to the user if it is not already linked
     * @param user The user object
     * @param oauthProvider The OAuth provider object
     */
    protected void linkOAuthMethod(User user, OauthProvider oauthProvider) {
        // init the oauthProviders set for new user to avoid NullPointerException
        if(user.getOauthProviders() == null) {
            user.setOauthProviders(
                // Create a new HashSet with the oauthProvider object
                // Use Collections.singleton() to create a immutable Set with a single element
                new HashSet<>(Collections.singleton(oauthProvider))
            );
            usersRepository.save(user);
        }
        // Add the oauthProvider to the user if it is not already linked
        else if (!user.getOauthProviders().contains(oauthProvider)) {
            user.addOauthProvider(oauthProvider);
            usersRepository.save(user);
        }
        // Do nothing if the oauthProvider is already linked to the user
    }

}
