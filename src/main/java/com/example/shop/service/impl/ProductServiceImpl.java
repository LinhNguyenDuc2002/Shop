package com.example.shop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.response.PageResponse;
import com.example.shop.dto.ProductDto;
import com.example.shop.dto.request.ProductRequest;
import com.example.shop.entity.Category;
import com.example.shop.entity.Image;
import com.example.shop.entity.Product;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ImageRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.ProductService;
import com.example.shop.util.DateUtil;
import com.example.shop.util.PageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public ProductDto add(String productRequest, List<MultipartFile> files) throws NotFoundException, ValidationException {
        log.info("Add a product");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductRequest newProduct = objectMapper.readValue(productRequest, ProductRequest.class);

            if (newProduct == null ||
                    newProduct.getProductName() == null ||
                    newProduct.getQuantity() == null ||
                    newProduct.getPrice() == null) {
                log.error("The request is invalid");
                throw ValidationException.builder()
                        .message(ResponseMessage.INPUT_INVALID.getMessage())
                        .build();
            }

            Product product = convertToProduct(newProduct);
            List<Image> images = uploadFile(files);
            imageRepository.saveAll(images);

            Category category = categoryRepository.findById(newProduct.getCategory())
                    .orElseThrow(() -> {
                        log.error("Not valid category found");
                        return NotFoundException.builder()
                                .message(ResponseMessage.CATEGORY_NOT_FOUND.getMessage())
                                .build();
                    });

            product.setSold(0L);
            product.setUpdate_day(new Date());
            product.setCategory(category);
            productRepository.save(product);
            images.stream().forEach(image -> image.setProduct(product));
            imageRepository.saveAll(images);

            log.info("Added a product successfully");
            return ProductMapper.INSTANCE.toDto(productRepository.findById(product.getId()).get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<ProductDto> getAll(Integer size, Integer page) {
        log.info("Get all products of page number {} with {} elements", page, size);

        Pageable pageable = PageUtil.getPage(page, size);

        int total = 1;
        List<Product> products;
        if (pageable != null) {
            Page<Product> productPage = productRepository.findAll(pageable);
            products = productPage.getContent();
            total = productPage.getTotalPages();
        } else {
            products = productRepository.findAll();
        }

        log.info("Got all products of page number {} with {} elements successfully", page, size);
        return PageResponse.<ProductDto>builder()
                .index(page)
                .elements(ProductMapper.INSTANCE.toDtoList(products))
                .totalPage(total)
                .build();
    }

    @Override
    public ProductDto get(Long id) throws NotFoundException {
        log.info("Get product {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product {0} don't exist", id);
                    return NotFoundException.builder()
                            .message(ResponseMessage.PRODUCT_NOT_FOUND.getMessage())
                            .build();
                });

        log.info("Got product {} successfully", id);
        return ProductMapper.INSTANCE.toDto(product);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        log.info("Delete product {}", id);

        boolean checkingProduct = productRepository.existsById(id);
        if (!checkingProduct) {
            log.error("Product {} don't exist", id);
            throw new NotFoundException(ResponseMessage.PRODUCT_NOT_FOUND.getMessage());
        }

        List<Image> images = imageRepository.findByProductId(id);
        images.stream().forEach(image -> {
            try {
                destroyFile(image.getPublicId(), ObjectUtils.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        log.info("Deleted product {}", id);
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto update(Long id, String productRequest, List<MultipartFile> files) throws NotFoundException {
        log.info("Update product {}", id);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductRequest newProduct = objectMapper.readValue(productRequest, ProductRequest.class);

            if (newProduct == null ||
                    newProduct.getProductName() == null ||
                    newProduct.getQuantity() == null ||
                    newProduct.getPrice() == null) {
                log.error("The request is invalid");
                throw ValidationException.builder()
                        .message(ResponseMessage.INPUT_INVALID.getMessage())
                        .build();
            }

            Product product = productRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Product {} don't exist", id);
                        return NotFoundException.builder()
                                .message(ResponseMessage.PRODUCT_NOT_FOUND.getMessage())
                                .build();
                    });

            Category category = categoryRepository.findById(newProduct.getCategory())
                    .orElseThrow(() -> {
                        log.error("Category {} don't exist", id);
                        return NotFoundException.builder()
                                .message(ResponseMessage.CATEGORY_NOT_FOUND.getMessage())
                                .build();
                    });

            product.setProductName(newProduct.getProductName());
            product.setPrice(newProduct.getPrice());
            product.setQuantity(newProduct.getQuantity());
            product.setNote(newProduct.getNote());
            product.setCategory(category);
            product.setUpdate_day(new Date());
            productRepository.save(product);

            if (files != null) {
                List<Image> images = product.getImages().stream().toList();
                images.stream().forEach(a -> {
                    try {
                        destroyFile(a.getPublicId(), ObjectUtils.emptyMap());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                imageRepository.deleteAll(images);

                List<Image> imageFiles = uploadFile(files);
                imageFiles.stream().forEach(image -> image.setProduct(product));
                imageRepository.saveAll(imageFiles);
            }

            log.info("Updated product {} successfully", id);
            return ProductMapper.INSTANCE.toDto(product);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<ProductDto> getByCategoryId(Long id, Integer size, Integer page) {
        log.info("Get products by category {}", id);

        Pageable pageable = PageUtil.getPage(page, size);
        Page<Product> productPage = productRepository.findProductsByCategoryId(id, pageable);

        log.info("Get products by category {} successfully", id);
        return PageResponse.<ProductDto>builder()
                .index(page)
                .elements(ProductMapper.INSTANCE.toDtoList(productPage.getContent()))
                .totalPage(productPage.getTotalPages())
                .build();
    }

    @Override
    public PageResponse<ProductDto> search(String key, Integer size, Integer page) {
        log.info("Search products by name '{}'", key);

        if (key == null) {
            key = "";
        }

        Pageable pageable = PageUtil.getPage(page, size);
        Page<Product> products = productRepository.findProductsByName(key.toLowerCase(), pageable);

        log.info("Search products by name '{}' successfully", key);
        return PageResponse.<ProductDto>builder()
                .index(page)
                .elements(ProductMapper.INSTANCE.toDtoList(products.getContent()))
                .totalPage(products.getTotalPages())
                .build();
    }

    @Override
    public List<ProductDto> getTopProducts(Integer top) {
        log.info("Get the top {} best selling products", top);

        List<Product> products = productRepository.findTopByOrderBySoldDesc(top);

        log.info("Got the top {} best selling products successfully", top);
        return ProductMapper.INSTANCE.toDtoList(products);
    }

    private Product convertToProduct(ProductRequest productRequest) {
        return Product.builder()
                .productName(productRequest.getProductName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .note(productRequest.getNote())
                .update_day(new Date())
                .build();
    }

    private List<Image> uploadFile(List<MultipartFile> files) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            Map data = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            images.add(Image.builder()
                    .format(data.get("format").toString())
                    .resourceType(data.get("resource_type").toString())
                    .secureUrl(data.get("secure_url").toString())
                    .createdAt(DateUtil.convertStringToDate(data.get("created_at").toString()))
                    .url(data.get("url").toString())
                    .publicId(data.get("public_id").toString())
                    .build());
        }
        return images;
    }

    private void destroyFile(String publicId, Map map) throws IOException {
        cloudinary.uploader().destroy(publicId, map);
    }
}
