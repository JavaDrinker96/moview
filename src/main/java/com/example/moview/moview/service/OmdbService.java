package com.example.moview.moview.service;

import java.util.Optional;

public interface OmdbService {

    Integer getRatingByMovieName(String name);

    Optional<String> getDescriptionByMovieName(String name);
}
