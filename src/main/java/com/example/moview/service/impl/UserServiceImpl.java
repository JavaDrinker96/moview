package com.example.moview.service.impl;

import com.example.moview.model.Role;
import com.example.moview.model.RoleName;
import com.example.moview.model.User;
import com.example.moview.repository.RoleRepository;
import com.example.moview.repository.UserRepository;
import com.example.moview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends AbstractService<User, UserRepository, Long> implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Search for a user with email = {}.", email);
        User user = repository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format("The user with the email = %s was not found in the database.", email))
        );

        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().getValue()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public User create(User user) {
        log.info("Saving new user with email = {}.", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return super.create(user);
    }

    @Override
    public void addRoleToUser(Long targetUserId, RoleName roleName) {
        User user = repository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("The user with the id = %d was not found in the database.", targetUserId)
                ));

        Role role = roleRepository.findByName(roleName.getValue())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("The role with the name = %s was not found in the database.", roleName.getValue())
                ));

        user.getRoles().add(role);
        repository.save(user);
    }

}