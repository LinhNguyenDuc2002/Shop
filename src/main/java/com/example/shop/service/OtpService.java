package com.example.shop.service;

import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import jakarta.servlet.http.HttpSession;

public interface OtpService {
    void sendOTP(String email, String username, String otp);

    void verifyOTP(String otp, HttpSession session);
}
