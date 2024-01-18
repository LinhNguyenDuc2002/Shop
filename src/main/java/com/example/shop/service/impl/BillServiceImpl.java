package com.example.shop.service.impl;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.BillDto;
import com.example.shop.dto.request.BillRequest;
import com.example.shop.dto.response.PageResponse;
import com.example.shop.entity.Address;
import com.example.shop.entity.Bill;
import com.example.shop.entity.Detail;
import com.example.shop.entity.User;
import com.example.shop.exception.NotFoundException;
import com.example.shop.mapper.AddressMapper;
import com.example.shop.mapper.BillMapper;
import com.example.shop.repository.BillRepository;
import com.example.shop.repository.DetailRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.BillService;
import com.example.shop.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public BillDto create(Long id, BillRequest billRequest) throws NotFoundException {
        log.info("Create a bill of user {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        Address address = AddressMapper.INSTANCE.toEntity(billRequest.getAddress());

        List<Detail> details = detailRepository.findAllById(billRequest.getDetails());

        Bill bill = Bill.builder()
                .address(address)
                .user(user)
                .details(details)
                .active(false)
                .purchaseDate(new Date())
                .build();
        billRepository.save(bill);

        detailRepository.saveAllAndFlush(details.stream()
                .map(detail -> {
                    detail.setBill(bill);
                    detail.setStatus(true);
                    return detail;
                })
                .collect(Collectors.toList()));

        log.info("Created a bill of user {} successfully", id);
        return BillMapper.INSTANCE.toDto(bill);
    }

    @Transactional
    @Override
    public PageResponse<BillDto> getAll(Integer size, Integer page, Date startAt, Date endAt) {
        log.info("Get all bills from {} to {}", startAt, endAt);

        Pageable pageable = PageUtil.getPage(page, size);
        Page<Bill> billPage = billRepository.findAllByPurchaseDateBetween(startAt, endAt, pageable);

        log.info("Got all bills from {} to {} successfully", startAt, endAt);
        return PageResponse.<BillDto>builder()
                .index(page)
                .totalPage(billPage.getTotalPages())
                .elements(BillMapper.INSTANCE.toDtoList(billPage.getContent()))
                .build();
    }

    @Override
    public BillDto get(Long id) throws NotFoundException {
        log.info("Get bill {}", id);

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> {
                    return NotFoundException.builder()
                            .message(ResponseMessage.BILL_NOT_FOUND.getMessage())
                            .build();
                });

        return BillMapper.INSTANCE.toDto(bill);
    }

    @Override
    public List<BillDto> getByUserId(Long id) {
        log.info("Get all bills of user {}",id);

        List<Bill> bills = billRepository.findByUserId(id);

        log.info("Got all bills of user {} successfully",id);
        return BillMapper.INSTANCE.toDtoList(bills);
    }

    @Override
    public void markAsPaid(Long id) throws NotFoundException {
        log.info("Confirm bill {} as paid", id);

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> {
                    return NotFoundException.builder()
                            .message(ResponseMessage.BILL_NOT_FOUND.getMessage())
                            .build();
                });

        bill.setActive(true);
        billRepository.save(bill);
    }
}
