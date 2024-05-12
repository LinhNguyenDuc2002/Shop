package com.example.shop.service;

import com.example.shop.dto.AddressDto;
import com.example.shop.dto.request.AddressRequest;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;

public interface AddressService {
    AddressDto update(AddressRequest addressRequest, String id) throws ValidationException, NotFoundException;

    AddressDto get(String id) throws NotFoundException;
}
