package com.example.shop.service;

import com.example.shop.dto.CommentDto;
import com.example.shop.dto.response.PageResponse;
import com.example.shop.dto.request.CommentRequest;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;

public interface CommentService {
    PageResponse<CommentDto> getAll(Long id, Integer size, Integer page);

    CommentDto create(Long id, Long userId, CommentRequest commentRequest) throws NotFoundException;

    void delete(Long id) throws ValidationException, NotFoundException;

    CommentDto update(Long id, CommentRequest commentRequest) throws ValidationException, NotFoundException;
}
