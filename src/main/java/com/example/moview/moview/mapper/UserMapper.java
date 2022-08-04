package com.example.moview.moview.mapper;

import com.example.moview.moview.dto.user.UserCreateDto;
import com.example.moview.moview.dto.user.UserDto;
import com.example.moview.moview.dto.user.UserUpdateDto;
import com.example.moview.moview.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    String DATE_PATTERN = "dd.MM.yyyy";

    @Mapping(source = "birthday", target = "birthday", dateFormat = DATE_PATTERN)
    User createDtoToModel(UserCreateDto dto);

    @Mapping(source = "birthday", target = "birthday", dateFormat = DATE_PATTERN)
    UserDto modelToDto(User model);

    @Mapping(source = "birthday", target = "birthday", dateFormat = DATE_PATTERN)
    User updateDtoToModel(UserUpdateDto dto);
}