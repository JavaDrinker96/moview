package com.example.moview.moview.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder(builderClassName = "UserUpdateDtoBuilder")
@JsonDeserialize(builder = UserUpdateDto.UserUpdateDtoBuilder.class)
public class UserUpdateDto {

    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String birthday;

    @NotNull
    @Email
    private String email;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserUpdateDtoBuilder {
    }
}