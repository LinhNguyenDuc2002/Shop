package com.example.shop.service.impl;

import com.example.shop.dto.CategoryDto;
import com.example.shop.dto.request.CategoryRequest;
import com.example.shop.entity.Category;
import com.example.shop.mapper.CategoryMapper;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDto add(CategoryRequest newCategory) {
        log.info("Add a category");
        Category category = convertToCategory(newCategory);
        categoryRepository.save(category);

        log.info("Added a category successfully");
        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Override
    public List<CategoryDto> getAll() {
        log.info("Get all categories");
        List<Category> categories = categoryRepository.findAll();

        log.info("Get all categories successfully");
        return CategoryMapper.INSTANCE.toCategoryDtoList(categories);
    }

    public Category convertToCategory(CategoryRequest newCategory) {
        return Category.builder()
                .name(newCategory.getName())
                .note(newCategory.getNote())
                .build();
    }
}
