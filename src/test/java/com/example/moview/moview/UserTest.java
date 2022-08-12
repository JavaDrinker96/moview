package com.example.moview.moview;

import com.example.moview.moview.dto.user.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserTest extends AbstractTest {

    @Test
    void createUser_Success() throws Exception {
        final String requestBody = objectWriter.writeValueAsString(UserCreateDto.builder()
                .firstName(defaultUserName)
                .lastName(defaultUserLastName)
                .birthday(formatLocalDate(defaultUserBirthday))
                .email(defaultUserEmail)
                .build());

        mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(defaultUserName)))
                .andExpect(jsonPath("$.lastName", is(defaultUserLastName)))
                .andExpect(jsonPath("$.birthday", is(formatLocalDate(defaultUserBirthday))))
                .andExpect(jsonPath("$.email", is(defaultUserEmail)));
    }

    @Test
    void createUserWithNonValidEmail_Exception() throws Exception {
        final String requestBody = objectWriter.writeValueAsString(UserCreateDto.builder()
                .firstName(defaultUserName)
                .lastName(defaultUserLastName)
                .birthday(formatLocalDate(defaultUserBirthday))
                .email(defaultUserEmail + "@")
                .build());

        final RequestBuilder requestBuilder = post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(MethodArgumentNotValidException.class)));
    }
}