package com.example.moview;

import com.example.moview.dto.user.UserCreateDto;
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
        final String request = objectWriter.writeValueAsString(UserCreateDto.builder()
                .firstName(userName)
                .lastName(userLastName)
                .birthday(formatLocalDate(userBirthday))
                .email(userEmail)
                .build());

        mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(userName)))
                .andExpect(jsonPath("$.lastName", is(userLastName)))
                .andExpect(jsonPath("$.birthday", is(formatLocalDate(userBirthday))))
                .andExpect(jsonPath("$.email", is(userEmail)));
    }

    @Test
    void createUserWithNonValidEmail_Exception() throws Exception {
        final String request = objectWriter.writeValueAsString(UserCreateDto.builder()
                .firstName(userName)
                .lastName(userLastName)
                .birthday(formatLocalDate(userBirthday))
                .email(userEmail + "@")
                .build());

        final RequestBuilder requestBuilder = post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request);

        final MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(MethodArgumentNotValidException.class)));
    }
}