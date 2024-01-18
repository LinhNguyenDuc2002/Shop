package com.example.shop.service;

import com.example.shop.dto.DetailDto;
import com.example.shop.exception.NotFoundException;

import java.util.List;

public interface DetailService {
    DetailDto add(Long userId, Long productId, Long quantity) throws NotFoundException;

    void delete(Long id) throws NotFoundException;

    DetailDto update(Long id, Long quantity) throws NotFoundException;

    List<DetailDto> getProductsUserPurchased(Long id) throws NotFoundException;

    List<DetailDto> getCart(Long id) throws NotFoundException;
}
