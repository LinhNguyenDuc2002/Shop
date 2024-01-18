package com.example.shop.service;

import com.example.shop.dto.CategoryDto;
import com.example.shop.dto.request.CategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryDto add(CategoryRequest newCategory);

    List<CategoryDto> getAll();
}
