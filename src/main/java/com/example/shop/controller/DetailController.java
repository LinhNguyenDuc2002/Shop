package com.example.shop.controller;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.DetailDto;
import com.example.shop.dto.response.CommonResponse;
import com.example.shop.exception.NotFoundException;
import com.example.shop.service.DetailService;
import com.example.shop.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
public class DetailController {
    @Autowired
    private DetailService detailsService;

    @PostMapping("/users/{userId}/products/{productId}")
    public ResponseEntity<CommonResponse<DetailDto>> add(@RequestParam(name = "quantity") Long quantity,
                                                         @PathVariable Long userId,
                                                         @PathVariable Long productId) throws NotFoundException {
        return ResponseUtil.wrapResponse(detailsService.add(userId, productId, quantity), ResponseMessage.ADD_ITEM_SUCCESS.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable Long id) throws NotFoundException {
        detailsService.delete(id);
        return ResponseUtil.wrapResponse(null, ResponseMessage.DELETE_ITEM_SUCCESS.getMessage());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<DetailDto>> update(@PathVariable Long id, @RequestParam(name = "quantity") Long quantity) throws NotFoundException {
        return ResponseUtil.wrapResponse(detailsService.update(id, quantity), ResponseMessage.UPDATE_ITEM_SUCCESS.getMessage());
    }

    @GetMapping("/users/{id}/purchased")
    public ResponseEntity<CommonResponse<List<DetailDto>>> getProductsUserPurchased(@PathVariable Long id) throws NotFoundException {
        return ResponseUtil.wrapResponse(detailsService.getProductsUserPurchased(id), ResponseMessage.GET_ALL_PRODUCT_SUCCESS.getMessage());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<CommonResponse<List<DetailDto>>> getCart(@PathVariable Long id) throws NotFoundException {
        return ResponseUtil.wrapResponse(detailsService.getCart(id), ResponseMessage.GET_ALL_PRODUCT_SUCCESS.getMessage());
    }
}
