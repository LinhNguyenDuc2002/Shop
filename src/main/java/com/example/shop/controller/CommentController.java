package com.example.shop.controller;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.constant.ParameterConstant;
import com.example.shop.dto.CommentDto;
import com.example.shop.dto.request.CommentRequest;
import com.example.shop.dto.response.CommonResponse;
import com.example.shop.dto.response.PageResponse;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.service.CommentService;
import com.example.shop.util.HandleBindingResult;
import com.example.shop.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/products/{id}")
    public ResponseEntity<CommonResponse<PageResponse<CommentDto>>> getAll(
            @PathVariable Long id,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page) {
        return ResponseUtil.wrapResponse(commentService.getAll(id, size, page), ResponseMessage.GET_COMMENTS_SUCCESS.getMessage());
    }

    @PostMapping("/products/{id}/users/{userId}")
    public ResponseEntity<CommonResponse<CommentDto>> create(
            @PathVariable Long id,
            @PathVariable Long userId,
            @Valid @RequestBody CommentRequest commentRequest,
            BindingResult bindingResult) throws ValidationException, NotFoundException {
        HandleBindingResult.handle(bindingResult, commentRequest);
        return ResponseUtil.wrapResponse(commentService.create(id, userId, commentRequest), ResponseMessage.CREATE_COMMENT_SUCCESS.getMessage());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<CommentDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest commentRequest,
            BindingResult bindingResult) throws NotFoundException, ValidationException {
        HandleBindingResult.handle(bindingResult, commentRequest);
        return ResponseUtil.wrapResponse(commentService.update(id, commentRequest), ResponseMessage.UPDATE_COMMENT_SUCCESS.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable Long id) throws NotFoundException, ValidationException {
        commentService.delete(id);
        return ResponseUtil.wrapResponse(null, ResponseMessage.DELETE_COMMENT_SUCCESS.getMessage());
    }
}
