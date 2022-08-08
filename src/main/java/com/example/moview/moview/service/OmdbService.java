package com.example.moview.moview.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OmdbService {

    Integer getRatingByMovieName(String name) throws JsonProcessingException;
}
