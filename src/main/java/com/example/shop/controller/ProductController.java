package com.example.shop.controller;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.constant.ParameterConstant;
import com.example.shop.dto.ProductDto;
import com.example.shop.dto.response.CommonResponse;
import com.example.shop.dto.response.PageResponse;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.service.ProductService;
import com.example.shop.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<ProductDto>> add(
            @RequestPart("file") MultipartFile file,
            @RequestPart("product") String productRequest) throws ValidationException, NotFoundException {
        return ResponseUtil.wrapResponse(productService.add(productRequest, file), ResponseMessage.ADD_PRODUCT_SUCCESS.getMessage());
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CommonResponse<PageResponse<ProductDto>>> getByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page) {
        return ResponseUtil.wrapResponse(productService.getByCategoryId(categoryId, size, page), ResponseMessage.ADD_PRODUCT_SUCCESS.getMessage());
    }

    @GetMapping()
    public ResponseEntity<CommonResponse<PageResponse<ProductDto>>> getAll(
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page) {
        return ResponseUtil.wrapResponse(productService.getAll(size, page), ResponseMessage.GET_ALL_PRODUCT_SUCCESS.getMessage());
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<PageResponse<ProductDto>>> search(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page) {
        return ResponseUtil.wrapResponse(productService.search(key, size, page), ResponseMessage.GET_ALL_PRODUCT_SUCCESS.getMessage());
    }

    @GetMapping("/popularity")
    public ResponseEntity<CommonResponse<List<ProductDto>>> getTopProducts(@RequestParam(name = "top", required = true) Integer top) {
        return ResponseUtil.wrapResponse(productService.getTopProducts(top), ResponseMessage.GET_ALL_PRODUCT_SUCCESS.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductDto>> get(@PathVariable Long id) throws NotFoundException {
        return ResponseUtil.wrapResponse(productService.get(id), ResponseMessage.GET_PRODUCT_SUCCESS.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable Long id) throws NotFoundException {
        productService.delete(id);
        return ResponseUtil.wrapResponse(null, ResponseMessage.DELETE_PRODUCT_SUCCESS.getMessage());
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<ProductDto>> update(@PathVariable Long id,
                                                             @RequestPart("file") MultipartFile file,
                                                             @RequestPart("product") String productRequest) throws ValidationException, NotFoundException {
        return ResponseUtil.wrapResponse(productService.update(id, productRequest, file), ResponseMessage.UPDATE_PRODUCT_SUCCESS.getMessage());
    }
}
