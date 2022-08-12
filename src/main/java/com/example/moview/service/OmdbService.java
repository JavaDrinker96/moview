package com.example.moview.service;

public interface OmdbService {

    Integer getRatingByMovieName(String name);

    String getDescriptionByMovieName(String name);
}
