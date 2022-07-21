package com.example.moview.moview.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "UserDtoBuilder")
@JsonDeserialize(builder = UserDto.UserDtoBuilder.class)
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserDtoBuilder {
    }
}