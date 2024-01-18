package com.example.shop.service.impl;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.DetailDto;
import com.example.shop.entity.Detail;
import com.example.shop.entity.Product;
import com.example.shop.entity.User;
import com.example.shop.exception.NotFoundException;
import com.example.shop.mapper.DetailMapper;
import com.example.shop.repository.DetailRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.DetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DetailServiceImpl implements DetailService {
    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public DetailDto add(Long userId, Long productId, Long quantity) throws NotFoundException {
        log.info("Add product {} of user {}", productId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", userId);
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product {} don't exist", productId);
                    return NotFoundException.builder()
                            .message(ResponseMessage.PRODUCT_NOT_FOUND.getMessage())
                            .build();
                });

        Optional<Detail> checkingDetail = detailRepository.findByUserIdAndProductId(userId, productId);
        Detail detail;

        if(checkingDetail.isPresent()) {
            detail = checkingDetail.get();
        }
        else {
            detail = Detail.builder()
                    .user(user)
                    .product(product)
                    .unitPrice(product.getPrice())
                    .status(false)
                    .build();
        }
        detail.setQuantity(quantity);
        detailRepository.save(detail);

        log.info("Added product {} of user {} successfully", productId, userId);
        return DetailMapper.INSTANCE.toDto(detail);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        log.info("Delete item {}", id);

        boolean checkingDetail = detailRepository.existsById(id);
        if(!checkingDetail) {
            log.error("Item {} doesn't exist");
            throw new NotFoundException(ResponseMessage.ITEM_NOT_FOUND.getMessage());
        }

        log.info("Deleted item {}", id);
        detailRepository.deleteById(id);
    }

    @Override
    public DetailDto update(Long id, Long quantity) throws NotFoundException {
        log.info("Update the quantity of order {}", id);

        Detail detail = detailRepository.findById(id)
                .orElseThrow(() -> {
                    return NotFoundException.builder()
                            .message(ResponseMessage.ITEM_NOT_FOUND.getMessage())
                            .build();
                });

        if(quantity > 0) {
            detail.setQuantity(quantity);
        }

        log.info("Updated the quantity of order {} successfully", id);
        return DetailMapper.INSTANCE.toDto(detail);
    }

    @Override
    public List<DetailDto> getProductsUserPurchased(Long id) throws NotFoundException {
        log.info("");

        boolean checkingUser = userRepository.existsById(id);

        if(!checkingUser) {
            log.error("");
            throw NotFoundException.builder()
                    .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                    .build();
        }

        List<Detail> details = detailRepository.findPurchased(id);

        log.info("");
        return DetailMapper.INSTANCE.toDtoList(details);
    }

    @Override
    public List<DetailDto> getCart(Long id) throws NotFoundException {
        log.info("");

        boolean checkingUser = userRepository.existsById(id);

        if(!checkingUser) {
            log.error("");
            throw NotFoundException.builder()
                    .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                    .build();
        }

        List<Detail> details = detailRepository.findCart(id);

        log.info("");
        return DetailMapper.INSTANCE.toDtoList(details);
    }
}
