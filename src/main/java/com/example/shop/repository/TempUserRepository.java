package com.example.shop.repository;

import com.example.shop.cache.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {
    Optional<TempUser> findByEmail(String email);

    @Query("SELECT t FROM TempUser t WHERE t.createdAt <= :date")
    List<TempUser> findByCreatedAt(@Param("date") Date expire);
}
