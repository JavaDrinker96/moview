package com.example.moview.service.impl;

import com.example.moview.model.User;
import com.example.moview.repository.UserRepository;
import com.example.moview.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbstractService<User, UserRepository, Long> implements UserService {

    public UserServiceImpl(final UserRepository repository) {
        super(repository);
    }
}
