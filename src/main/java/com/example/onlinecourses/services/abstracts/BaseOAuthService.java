package com.example.onlinecourses.services.abstracts;
import com.example.onlinecourses.models.OauthProvider;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.repositories.OAuth2ProviderRepository;
import com.example.onlinecourses.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collections;
import java.util.HashSet;

@RequiredArgsConstructor
public class BaseOAuthService extends DefaultOAuth2UserService {
    private final UsersRepository usersRepository;
    private final OAuth2ProviderRepository oAuth2ProviderRepository;
    /**
     * Load user from OAuth2 provider and add role and provider
     * @param userRequest the user request
     * @return (OAuth2UserImpl)
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Load user from OAuth2 provider
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        oauth2User.getAttributes().put("provider", provider);

        // return OAuth2User with role and provider
        return oauth2User;
    }

    public User checkUser(String email, String name, String picture) {
        return usersRepository.findUserByEmail(email).orElse(createUser(email, name, picture));
    }

    private User createUser(String email, String name, String picture) {
        User user = User.builder()
            .email(email)
            .fullname(name)
            .avatar(picture)
            .build();
        return usersRepository.save(user);
    }

    public OauthProvider checkProvider(String openId, String provider, String email, User user) {
        return oAuth2ProviderRepository.findOauthProviderByOpenId(openId).orElse(createProvider(openId, provider, email, user));
    }

    private OauthProvider createProvider(String openId, String provider, String email, User user) {
        OauthProvider oauthProvider = OauthProvider.builder()
            .openId(openId)
            .provider(provider)
            .linkedEmail(email)
            .user(user)
            .build();
        return oAuth2ProviderRepository.save(oauthProvider);
    }

    public void linkOAuthMethod(User user, OauthProvider oauthProvider) {
        // init the oauthProviders set for new user to avoid NullPointerException
        if(user.getOauthProviders() == null) {
            user.setOauthProviders(
                new HashSet<>(Collections.singleton(oauthProvider))
            );
            usersRepository.save(user);
        }
        else if (!user.getOauthProviders().contains(oauthProvider)) {
            user.addOauthProvider(oauthProvider);
            usersRepository.save(user);
        }
    }

}
