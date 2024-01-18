package com.example.shop.repository;

import com.example.shop.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Page<Bill> findAllByPurchaseDateBetween(Date startAt, Date endAt, Pageable pageable);

    List<Bill> findByUserId(Long id);
}
