package com.example.shop.controller;

import com.example.shop.dto.request.Credentials;
import com.example.shop.dto.response.AuthResponse;
import com.example.shop.exception.ValidationException;
import com.example.shop.service.AuthService;
import com.example.shop.util.HandleBindingResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody Credentials credentials, BindingResult bindingResult) throws ValidationException {
        HandleBindingResult.handle(bindingResult, credentials);

        return ResponseEntity.ok(authService.authenticate(credentials));
    }
}
