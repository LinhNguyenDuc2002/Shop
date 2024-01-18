package com.example.shop.repository;

import com.example.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    Page<Product> findProductsByCategoryId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.sold DESC LIMIT :top")
    List<Product> findTopByOrderBySoldDesc(@Param("top") Integer top);

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:key%")
    Page<Product> findProductsByName(@Param("key") String key, Pageable pageable);
}
