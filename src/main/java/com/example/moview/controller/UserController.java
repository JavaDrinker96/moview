package com.example.moview.controller;

import com.example.moview.dto.user.UserCreateDto;
import com.example.moview.dto.user.UserDto;
import com.example.moview.dto.user.UserUpdateDto;
import com.example.moview.mapper.UserMapper;
import com.example.moview.model.User;
import com.example.moview.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Api(tags = "Endpoints for users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation("Register new user")
    public UserDto create(@RequestBody @Valid final UserCreateDto dto) {
        final User user = userService.create(userMapper.userCreateDtoToEntity(dto));
        return userMapper.entityToUserDto(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Get user by id")
    public UserDto read(@PathVariable final Long id) {
        return userMapper.entityToUserDto(userService.read(id));
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Change user")
    public UserDto update(@RequestBody @Valid final UserUpdateDto dto) {
        final User user = userService.update(userMapper.userUpdateDtoToEntity(dto));
        return userMapper.entityToUserDto(user);
    }

}