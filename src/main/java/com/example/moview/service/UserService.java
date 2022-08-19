package com.example.moview.service;

import com.example.moview.model.User;

public interface UserService {

    User register(User user);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);
}