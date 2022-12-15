package com.example.moview.validator;

import com.example.moview.model.User;
import com.example.moview.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserExisting(final Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Unable to find %s with id %d", User.class.getName(), userId))
        );
    }

}