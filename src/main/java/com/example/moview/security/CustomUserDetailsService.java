package com.example.moview.security;

import com.example.moview.model.User;
import com.example.moview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("User with email %s not found.", email)
                        )
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(final Long id) {

        final User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format("Can't find user with id %d.", id)
                        )
                );

        return UserPrincipal.create(user);
    }
}
