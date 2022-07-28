package com.example.moview.moview.controller;

import com.example.moview.moview.dto.user.UserCreateDto;
import com.example.moview.moview.dto.user.UserDto;
import com.example.moview.moview.dto.user.UserUpdateDto;
import com.example.moview.moview.mapper.UserMapper;
import com.example.moview.moview.model.User;
import com.example.moview.moview.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(final UserService userService, final UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid final UserCreateDto dto) {
        final User user = userService.create(userMapper.createDtoToModel(dto));
        final UserDto userDto = userMapper.modelToDto(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> read(@PathVariable final Long id) {
        final UserDto userDto = userMapper.modelToDto(userService.read(id));
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody @Valid final UserUpdateDto dto) {
        final User user = userService.update(userMapper.updateDtoToModel(dto));
        final UserDto userDto = userMapper.modelToDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
