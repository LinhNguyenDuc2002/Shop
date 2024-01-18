package com.example.shop.controller;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.CategoryDto;
import com.example.shop.dto.request.CategoryRequest;
import com.example.shop.dto.response.CommonResponse;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.service.CategoryService;
import com.example.shop.util.HandleBindingResult;
import com.example.shop.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<CommonResponse<CategoryDto>> add(@Valid @RequestBody CategoryRequest newCategory,
                                                           BindingResult bindingResult) throws NotFoundException, ValidationException {
        HandleBindingResult.handle(bindingResult, newCategory);
        return ResponseUtil.wrapResponse(categoryService.add(newCategory), ResponseMessage.ADD_CATEGORY_SUCCESS.getMessage());
    }

    @GetMapping()
    public ResponseEntity<CommonResponse<List<CategoryDto>>> getAll() {
        return ResponseUtil.wrapResponse(categoryService.getAll(), ResponseMessage.GET_ALL_CATEGORY_SUCCESS.getMessage());
    }
}
