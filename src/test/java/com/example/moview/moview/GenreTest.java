package com.example.moview.moview;

import com.example.moview.moview.config.db.HibernateTestConfig;
import com.example.moview.moview.config.spring.SpringConfig;
import com.example.moview.moview.dto.genre.GenreCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        HibernateTestConfig.class,
        SpringConfig.class
})
@WebAppConfiguration
public class GenreTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private MediaType applicationJsonUtf8;

    private ObjectWriter objectWriter;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        ObjectMapper mapper = new ObjectMapper();
        applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    public void getGreet_Ok() throws Exception {
        mvc.perform(get("/greet")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.message", is("Hello World!!!")));
    }

    @Test
    public void createGenre_ThenOk() throws Exception {
        final GenreCreateDto genreCreateDto = GenreCreateDto.builder().name("genre").build();
        final String request = objectWriter.writeValueAsString(genreCreateDto);

        mvc.perform(post("/genre").contentType(applicationJsonUtf8).content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("genre")));
    }
}