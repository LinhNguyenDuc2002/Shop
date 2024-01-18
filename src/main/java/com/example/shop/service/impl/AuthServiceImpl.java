package com.example.shop.service.impl;

import com.example.shop.config.JwtConfig;
import com.example.shop.dto.request.Credentials;
import com.example.shop.dto.response.AuthResponse;
import com.example.shop.service.AuthService;
import com.example.shop.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse authenticate(Credentials credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername().toLowerCase(), credentials.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtConfig.generateJwtToken(authentication);
        String refreshToken = jwtConfig.generateRefreshToken(authentication);

        log.info("Authenticating credential");
        return AuthResponse.builder()
                .message("Authenticate successfully!")
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
