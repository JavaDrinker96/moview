package com.example.moview.security;

import com.example.moview.mapper.UserMapper;
import com.example.moview.model.User;
import com.example.moview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public CustomUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findByEmail(username);
        return userMapper.entityToCustomUserDetails(user);
    }
}
