package com.example.shop.service;

import com.example.shop.dto.UserDto;
import com.example.shop.dto.request.PasswordRequest;
import com.example.shop.dto.request.UserRequest;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface UserService {
    UserDto getLoggedInUser() throws NotFoundException;

    void createTempUser(UserRequest newUserRequest) throws ValidationException;

    void delete(String id) throws NotFoundException;

    List<UserDto> getAll();

    UserDto get(String id) throws NotFoundException;

    UserDto update(String id, UserRequest userRequest) throws NotFoundException, ValidationException;

    UserDto createUser(String id, String otp, String secret) throws ValidationException, NotFoundException;
}
