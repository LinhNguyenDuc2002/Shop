package com.example.shop.service.impl;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.CommentDto;
import com.example.shop.dto.response.PageResponse;
import com.example.shop.dto.request.CommentRequest;
import com.example.shop.entity.Comment;
import com.example.shop.entity.Product;
import com.example.shop.entity.User;
import com.example.shop.exception.NotFoundException;
import com.example.shop.mapper.CommentMapper;
import com.example.shop.repository.CommentRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.CommentService;
import com.example.shop.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PageResponse<CommentDto> getAll(Long id, Integer size, Integer page) {
        log.info("Get all comments of product {} with page {} and size {}", id, page, size);

        Pageable pageable = PageUtil.getPage(page, size);
        Page<Comment> comments = commentRepository.findByProductId(id, pageable);

        log.info("Got all comments of product {} with page {} and size {} successfully", id, page, size);
        return PageResponse.<CommentDto>builder()
                .index(page)
                .elements(CommentMapper.INSTANCE.toCommentDtoList(comments.getContent()))
                .totalPage(comments.getTotalPages())
                .build();
    }

    @Override
    public CommentDto create(Long id, Long userId, CommentRequest commentRequest) throws NotFoundException {
        log.info("Create a new comment for product {}", id);

        Comment comment = convertToComment(commentRequest);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ResponseMessage.PRODUCT_NOT_FOUND.getMessage())
                            .build();
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", userId);
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        comment.setProduct(product);
        comment.setUser(user);
        commentRepository.save(comment);

        log.info("Created a new comment for product {} successfully", id);
        return CommentMapper.INSTANCE.toCommentDto(comment);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        log.info("Delete comment {}", id);

        boolean checkingComment = commentRepository.existsById(id);
        if(!checkingComment) {
            log.error("Comment {} don't exist", id);
            throw NotFoundException.builder()
                    .message(ResponseMessage.COMMENT_NOT_FOUND.getMessage())
                    .build();
        }

        log.info("Deleted comment {} successfully", id);
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto update(Long id, CommentRequest commentRequest) throws NotFoundException {
        log.info("Update comment {}", id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()->{
                    log.error("Comment {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ResponseMessage.COMMENT_NOT_FOUND.getMessage())
                            .build();
        });

//        comment.setImage(commentRequest.getImage());
        comment.setMessage(commentRequest.getMessage());
        commentRepository.save(comment);

        log.info("Updated comment {} successfully", id);
        return CommentMapper.INSTANCE.toCommentDto(comment);
    }

    private Comment convertToComment(CommentRequest commentRequest) {
        return Comment.builder()
                .commentDate(new Date())
                .message(commentRequest.getMessage())
//                .image(commentRequest.getImage())
                .build();
    }
}
