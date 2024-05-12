package com.example.shop.service;

import com.example.shop.dto.request.Credentials;
import com.example.shop.dto.request.PasswordRequest;
import com.example.shop.dto.response.AuthResponse;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;

public interface AuthService {
    AuthResponse authenticate(Credentials credentials);

    void changePwd(String id, PasswordRequest passwordRequest) throws NotFoundException, ValidationException;

    void resetPwd(String id, String password) throws NotFoundException, ValidationException;
}
