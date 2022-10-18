package com.example.moview.security.oauth2.user;

import com.example.moview.exception.OAuth2AuthenticationProcessingException;
import com.example.moview.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(final String registrationId, final Map<String, Object> attributes) {

        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {

            return new GoogleOAuth2UserInfo(attributes);

        } else {

            throw new OAuth2AuthenticationProcessingException(
                    String.format("Sorry! Login with %s is not supported yet.", registrationId)
            );
        }
    }
}
