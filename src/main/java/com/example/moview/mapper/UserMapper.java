package com.example.moview.mapper;

import com.example.moview.dto.auth.AuthRequest;
import com.example.moview.dto.user.UserCreateDto;
import com.example.moview.dto.user.UserDto;
import com.example.moview.dto.user.UserUpdateDto;
import com.example.moview.model.User;
import com.example.moview.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {RoleMapper.class}, componentModel = "spring")
public interface UserMapper {

    String DATE_PATTERN = "dd.MM.yyyy";

    @Mapping(source = "birthday", target = "birthday", dateFormat = DATE_PATTERN)
    User userCreateDtoToEntity(UserCreateDto dto);

    @Mapping(source = "birthday", target = "birthday", dateFormat = DATE_PATTERN)
    UserDto entityToUserDto(User entity);

    @Mapping(source = "birthday", target = "birthday", dateFormat = DATE_PATTERN)
    User userUpdateDtoToEntity(UserUpdateDto dto);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "roles", target = "authorities")
    CustomUserDetails entityToCustomUserDetails(User user);

    User authRequestToUser(AuthRequest request);
}