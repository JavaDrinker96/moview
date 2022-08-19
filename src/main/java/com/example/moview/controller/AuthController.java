package com.example.moview.controller;

import com.example.moview.dto.auth.AuthRequest;
import com.example.moview.dto.auth.AuthResponse;
import com.example.moview.dto.user.UserCreateDto;
import com.example.moview.dto.user.UserDto;
import com.example.moview.mapper.UserMapper;
import com.example.moview.model.User;
import com.example.moview.security.JwtProvider;
import com.example.moview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid final UserCreateDto dto) {
        final User user = userMapper.userCreateDtoToEntity(dto);
        final UserDto userDto = userMapper.entityToUserDto(userService.register(user));
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> auth(@Valid @RequestBody final AuthRequest request) {
        final User user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
        final String token = jwtProvider.generateToken(user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token));
    }
}