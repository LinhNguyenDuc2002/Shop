package com.example.shop.service;

import com.example.shop.dto.response.PageResponse;
import com.example.shop.dto.ProductDto;
import com.example.shop.dto.request.ProductRequest;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDto add(String productRequest, List<MultipartFile> files) throws ValidationException, NotFoundException;

    PageResponse<ProductDto> getAll(Integer size, Integer page);

    ProductDto get(Long id) throws NotFoundException;

    void delete(Long id) throws NotFoundException;

    ProductDto update(Long id, String productRequest, List<MultipartFile> files) throws NotFoundException;

    PageResponse<ProductDto> getByCategoryId(Long id, Integer size, Integer page);

    PageResponse<ProductDto> search(String key, Integer size, Integer page);

    List<ProductDto> getTopProducts(Integer top);
}
