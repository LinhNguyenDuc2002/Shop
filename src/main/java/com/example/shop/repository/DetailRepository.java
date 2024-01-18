package com.example.shop.repository;

import com.example.shop.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetailRepository extends JpaRepository<Detail, Long> {
    @Query("SELECT d FROM Detail d WHERE d.user.id = :userId AND d.product.id = :productId AND status = false")
    Optional<Detail> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query("SELECT d FROM Detail d WHERE d.user.id = :userId AND status = true")
    List<Detail> findPurchased(@Param("userId") Long id);

    @Query("SELECT d FROM Detail d WHERE d.user.id = :userId AND status = false")
    List<Detail> findCart(@Param("userId") Long id);
}
