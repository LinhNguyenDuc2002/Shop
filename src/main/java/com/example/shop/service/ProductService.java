package com.example.shop.service;

import com.example.shop.entity.User;
import com.example.shop.exception.ValidationException;

public interface ProductService {
    void createActor(User user) throws ValidationException;
}
