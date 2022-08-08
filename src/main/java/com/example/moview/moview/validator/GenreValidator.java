package com.example.moview.moview.validator;

import com.example.moview.moview.repository.GenreRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenreValidator {

    private final GenreRepository genreRepository;

    public GenreValidator(final GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public void validateGenresIds(final List<Long> genresIds) {
        final Set<Long> existsGenresIds = genresIds
                .stream()
                .filter(genreRepository::existsById)
                .collect(Collectors.toSet());

        if (!existsGenresIds.containsAll(genresIds)) {
            throw new EntityNotFoundException();
        }
    }
}