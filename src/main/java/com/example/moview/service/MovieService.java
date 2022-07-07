package com.example.moview.service;

public interface MovieService {

    void create(String jsonMovie);

    void update(String jsonMovie);

    void delete(Long id);

    String read(Long id);

    String readAll();
}
