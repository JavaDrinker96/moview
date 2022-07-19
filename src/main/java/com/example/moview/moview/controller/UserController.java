package com.example.moview.moview.controller;

import com.example.moview.moview.dto.user.UserCreateDto;
import com.example.moview.moview.dto.user.UserDto;
import com.example.moview.moview.dto.user.UserUpdateDto;
import com.example.moview.moview.model.User;
import com.example.moview.moview.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(final UserService userService, final ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResponseEntity<UserDto> create(@RequestBody @Valid final UserCreateDto dto) {
        final User user = modelMapper.map(dto, User.class);
        final User createdUser = userService.create(user);
        final UserDto dtoCreated = modelMapper.map(createdUser, UserDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreated);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> read(@PathVariable final Long id) {
        final User user = userService.read(id);
        final UserDto dto = modelMapper.map(user, UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> update(@RequestBody @Valid final UserUpdateDto dto) {
        final User user = modelMapper.map(dto, User.class);
        final User updatedUser = userService.update(user);
        final UserDto dtoUpdated = modelMapper.map(updatedUser, UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(dtoUpdated);
    }
}
