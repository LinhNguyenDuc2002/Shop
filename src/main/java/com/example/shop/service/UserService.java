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

    void createTempUser(UserRequest newUserRequest, HttpSession session) throws ValidationException;

    void delete(Long id) throws NotFoundException;

    List<UserDto> getAll();

    UserDto get(Long id) throws NotFoundException;

    UserDto update(Long id, UserRequest userRequest) throws NotFoundException, ValidationException;

    UserDto createUser(String otp, HttpSession session) throws ValidationException;

    void changePwd(Long id, PasswordRequest passwordRequest) throws NotFoundException, ValidationException;

    void resetPwd(Long id, String password) throws NotFoundException, ValidationException;
}
