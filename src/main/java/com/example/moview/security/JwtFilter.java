package com.example.moview.security;

import com.example.moview.mapper.UserMapper;
import com.example.moview.model.User;
import com.example.moview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.jsonwebtoken.lang.Strings.hasText;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String PREFIX = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            final String userEmail = jwtProvider.getEmailFromToken(token);
            final User user = userService.findByEmail(userEmail);
            final CustomUserDetails customUserDetails = userMapper.entityToCustomUserDetails(user);
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(PREFIX)) {
            return bearer.substring(7);
        }
        return null;
    }
}
