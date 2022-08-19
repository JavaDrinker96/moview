package com.example.moview.service.impl;

import com.example.moview.model.Role;
import com.example.moview.model.User;
import com.example.moview.repository.RoleRepository;
import com.example.moview.repository.UserRepository;
import com.example.moview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User register(final User user) {

        final Role userRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role with name " + DEFAULT_ROLE));

        user.setRoles(Set.of(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(final String email) {
        return findUserByEmail(email);
    }

    @Override
    public User findByEmailAndPassword(final String email, final String password) {
        final User user = findUserByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new EntityNotFoundException(String.format("User with email %s and password % does not exist.", email, password));
        }
        return user;
    }

    private User findUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with email " + email));
    }
}