package com.example.onlinecourses.services;
import com.example.onlinecourses.dtos.auth.AuthResponseDTO;
import com.example.onlinecourses.mappers.UserMapper;
import com.example.onlinecourses.models.OauthProvider;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.repositories.OAuth2ProviderRepository;
import com.example.onlinecourses.repositories.UsersRepository;
import com.example.onlinecourses.services.abstracts.BaseOAuthService;
import com.example.onlinecourses.services.interfaces.IExtractOAuthUser;
import com.example.onlinecourses.services.interfaces.IOAuth2Service;
import com.example.onlinecourses.utils.JwtUtil;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoogleOAuthService extends BaseOAuthService implements IExtractOAuthUser, IOAuth2Service {
    public GoogleOAuthService(UsersRepository usersRepository, OAuth2ProviderRepository oAuth2ProviderRepository) {
        super(usersRepository, oAuth2ProviderRepository);
    }

    @Override
    public String extractEmail(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("email");
    }
    @Override
    public String extractName(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("name");
    }
    @Override
    public String extractPicture(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("picture");
    }
    @Override
    public String extractOpenId(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("sub");
    }


    @Override
    @Transactional
    public AuthResponseDTO continueWithOAuth(OAuth2AuthenticationToken oAuthToken) {
        OAuth2User oAuth2User = oAuthToken.getPrincipal();
        // Extract user details from the OAuth2User object
        String openId = extractOpenId(oAuth2User);
        String email = extractEmail(oAuth2User);
        String name = extractName(oAuth2User);
        String picture = extractPicture(oAuth2User);
        String provider = oAuth2User.getAttribute("provider");

        // Check if the user already exists in the database or create a new one
        User user = super.checkUser(email, name, picture);

        // Check if the OAuth provider already exists for this user or create a new one
        OauthProvider oauthProvider = super.checkProvider(openId, provider, email, user);

        // Link the OAuth provider to the user if it is not already linked
        super.linkOAuthMethod(user, oauthProvider);

        String accessToken = JwtUtil.generateAccessToken(user);
        String refreshToken = JwtUtil.generateRefreshToken(user);

        return AuthResponseDTO.builder()
            .user(UserMapper.INSTANCE.toUserDataDTO(user))
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
