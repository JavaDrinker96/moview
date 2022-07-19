package com.example.moview.moview.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@Builder(builderClassName = "UserCreateDtoBuilder")
@JsonDeserialize(builder = UserCreateDto.UserCreateDtoBuilder.class)
public class UserCreateDto {

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
    public static class UserCreateDtoBuilder {
    }
}
