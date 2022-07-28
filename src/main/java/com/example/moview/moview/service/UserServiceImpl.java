package com.example.moview.moview.service;

import com.example.moview.moview.model.User;
import com.example.moview.moview.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbstractService<User, UserRepository, Long> implements UserService {

    public UserServiceImpl(final UserRepository repository) {
        super(repository);
    }
}
