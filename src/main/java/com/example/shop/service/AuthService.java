package com.example.shop.service;

import com.example.shop.dto.request.Credentials;
import com.example.shop.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(Credentials credentials);
}
