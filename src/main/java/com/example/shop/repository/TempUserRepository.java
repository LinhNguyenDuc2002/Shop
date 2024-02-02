package com.example.shop.repository;

import com.example.shop.cache.TempUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TempUserRepository extends CrudRepository<TempUser, Long> {
    @Query("SELECT t FROM TempUser t WHERE t.createdAt <= :date")
    List<TempUser> findByCreatedAt(@Param("date") Date expire);
}
