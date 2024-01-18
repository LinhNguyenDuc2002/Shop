package com.example.shop.service;

import com.example.shop.dto.UserDto;
import com.example.shop.dto.request.AddressRequest;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;

public interface AddressService {
    UserDto updateAddress(AddressRequest addressRequest, Long id) throws ValidationException, NotFoundException;
}
