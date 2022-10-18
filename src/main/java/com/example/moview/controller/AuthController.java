package com.example.moview.controller;

import com.example.moview.dto.security.TokenResponse;
import com.example.moview.dto.security.SignInRequest;
import com.example.moview.dto.security.SignUpRequest;
import com.example.moview.exception.BadRequestException;
import com.example.moview.mapper.AuthMapper;
import com.example.moview.mapper.UserMapper;
import com.example.moview.model.AuthProvider;
import com.example.moview.model.User;
import com.example.moview.repository.UserRepository;
import com.example.moview.security.TokenService;
import com.example.moview.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                authMapper.signInRequestToAuthentication(signInRequest)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(tokenService.generateTokensByAuthentication(authentication));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(final String refreshToken) {

        final TokenResponse tokenResponse = tokenService.refreshTokens(refreshToken);

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        setEncodedPassword(signUpRequest);
        final User user = userMapper.userFromLocalRegistration(signUpRequest, AuthProvider.local);
        userRepository.save(user);
    }

    public void setEncodedPassword(final SignUpRequest request) {
        final String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
    }
}