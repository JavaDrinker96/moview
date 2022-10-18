package com.example.moview.mapper;

import com.example.moview.dto.security.SignInRequest;
import org.mapstruct.Mapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    default Authentication signInRequestToAuthentication(SignInRequest signInRequest) {

        return new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),
                signInRequest.getPassword()
        );
    }

}
