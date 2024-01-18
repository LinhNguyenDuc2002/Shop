package com.example.shop.service;

import com.example.shop.dto.BillDto;
import com.example.shop.dto.request.BillRequest;
import com.example.shop.dto.response.PageResponse;
import com.example.shop.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public interface BillService {
    BillDto create(Long id, BillRequest billRequest) throws NotFoundException;

    PageResponse<BillDto> getAll(Integer size, Integer page, Date startAt, Date endAt);

    BillDto get(Long id) throws NotFoundException;

    List<BillDto> getByUserId(Long id);

    void markAsPaid(Long id) throws NotFoundException;
}
