package com.example.moview.moview.mapper;

import com.example.moview.moview.dto.user.UserCreateDto;
import com.example.moview.moview.dto.user.UserDto;
import com.example.moview.moview.dto.user.UserUpdateDto;
import com.example.moview.moview.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "birthday", target = "birthday", dateFormat = "dd.MM.yyyy")
    User createDtoToModel(UserCreateDto dto);

    @Mapping(source = "birthday", target = "birthday", dateFormat = "dd.MM.yyyy")
    UserDto modelToDto(User model);

    @Mapping(source = "birthday", target = "birthday", dateFormat = "dd.MM.yyyy")
    User updateDtoToModel(UserUpdateDto dto);
}