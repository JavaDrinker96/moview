package com.example.moview.security.oauth2;

import com.example.moview.exception.OAuth2AuthenticationProcessingException;
import com.example.moview.mapper.UserMapper;
import com.example.moview.model.AuthProvider;
import com.example.moview.model.User;
import com.example.moview.repository.UserRepository;
import com.example.moview.security.UserPrincipal;
import com.example.moview.security.oauth2.user.OAuth2UserInfo;
import com.example.moview.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) {

        final OAuth2User oAuth2User = super.loadUser(userRequest);

        try {

            return processOAuth2User(userRequest, oAuth2User);

        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            //TODO: handle it
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(final OAuth2UserRequest oAuth2UserRequest, final OAuth2User oAuth2User) {

        final OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (oAuth2UserInfo.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        final Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user;

        if (userOptional.isPresent()) {

            user = userOptional.get();

            if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {

                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");

            }

            user = updateExistingUser(user, oAuth2UserInfo);

        } else {

            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);

        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(final OAuth2UserRequest oAuth2UserRequest, final OAuth2UserInfo oAuth2UserInfo) {

        final User user = userMapper.userFromOAuth(oAuth2UserRequest, oAuth2UserInfo);

        return userRepository.save(user);
    }

    private User updateExistingUser(final User existingUser, final OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }

}