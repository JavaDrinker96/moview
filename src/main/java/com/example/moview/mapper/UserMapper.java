package com.example.moview.mapper;

import com.example.moview.dto.security.SignUpRequest;
import com.example.moview.dto.user.UserDto;
import com.example.moview.model.AuthProvider;
import com.example.moview.model.User;
import com.example.moview.security.oauth2.user.OAuth2UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {
    String DATE_PATTERN = "dd.MM.yyyy";

    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "birthday", source = "signUpRequest.birthday", dateFormat = DATE_PATTERN)
    @Mapping(target = "name", expression = "java((signUpRequest.getFirstName() != null && signUpRequest.getLastName() != null) " +
            "? signUpRequest.getFirstName() + \" \" + signUpRequest.getLastName() : signUpRequest.getName())")
    User userFromLocalRegistration(SignUpRequest signUpRequest, AuthProvider provider);


    @Mapping(target = "name", expression = "java(oAuth2UserInfo.getName())")
    @Mapping(target = "firstName", expression = "java(oAuth2UserInfo.getFirstName())")
    @Mapping(target = "lastName", expression = "java(oAuth2UserInfo.getLastName())")
    @Mapping(target = "email", expression = "java(oAuth2UserInfo.getEmail())")
    @Mapping(target = "emailVerified", expression = "java(oAuth2UserInfo.getEmailVerified())")
    @Mapping(target = "providerId", expression = "java(oAuth2UserInfo.getProviderId())")
    @Mapping(target = "provider", expression = "java(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))")
    User userFromOAuth(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo);

    @Mapping(target = "birthday", source = "birthday", dateFormat = DATE_PATTERN)
    UserDto entityToDto(User user);
}