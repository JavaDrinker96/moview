package com.example.moview.moview;

import com.example.moview.moview.dto.genre.GenreCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GenreTest extends AbstractTest {

    @Test
    public void createGenre_Success() throws Exception {
        final GenreCreateDto genreCreateDto = GenreCreateDto.builder().name("genre").build();
        final String request = objectWriter.writeValueAsString(genreCreateDto);

        mvc.perform(post("/genre").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("genre")));
    }
}