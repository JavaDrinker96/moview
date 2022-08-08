package com.example.moview.moview;

import com.example.moview.moview.dto.genre.GenreCreateDto;
import com.example.moview.moview.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GenreTest extends AbstractTest {

    @Test
    void createGenre_Success() throws Exception {
        final GenreCreateDto genreCreateDto = GenreCreateDto.builder().name("genre").build();
        final String request = objectWriter.writeValueAsString(genreCreateDto);

        mvc.perform(post("/genre").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("genre")));
    }

    @Test
    void readAllGenres_Success() throws Exception {
        final Genre genre1 = createDefaultGenre(1);
        final Genre genre2 = createDefaultGenre(2);
        final Genre genre3 = createDefaultGenre(3);

        mvc.perform(get("/genre/all"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()", is(3)))

                .andExpect(jsonPath("$.[0].id", is(genre1.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(genre1.getName())))

                .andExpect(jsonPath("$.[1].id", is(genre2.getId().intValue())))
                .andExpect(jsonPath("$.[1].name", is(genre2.getName())))

                .andExpect(jsonPath("$.[2].id", is(genre3.getId().intValue())))
                .andExpect(jsonPath("$.[2].name", is(genre3.getName())));
    }
}