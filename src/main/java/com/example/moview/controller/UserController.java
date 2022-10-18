package com.example.moview.controller;

import com.example.moview.dto.user.UserDto;
import com.example.moview.exception.ResourceNotFoundException;
import com.example.moview.mapper.UserMapper;
import com.example.moview.model.User;
import com.example.moview.repository.UserRepository;
import com.example.moview.security.CurrentUser;
import com.example.moview.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        //TODO: use service here
        final User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        final UserDto dto = userMapper.entityToDto(user);
        return ResponseEntity.ok(dto);
    }
}