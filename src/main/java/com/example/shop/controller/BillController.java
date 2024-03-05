package com.example.shop.controller;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.constant.ParameterConstant;
import com.example.shop.dto.BillDto;
import com.example.shop.dto.request.AddressRequest;
import com.example.shop.dto.request.BillRequest;
import com.example.shop.dto.response.CommonResponse;
import com.example.shop.dto.response.PageResponse;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.service.BillService;
import com.example.shop.util.HandleBindingResult;
import com.example.shop.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/users/{id}")
    public ResponseEntity<CommonResponse<BillDto>> create(@PathVariable Long id,
                                                          @Valid @RequestBody BillRequest billRequest,
                                                          BindingResult bindingResult) throws ValidationException, NotFoundException {
        HandleBindingResult.handle(bindingResult, billRequest);
        return ResponseUtil.wrapResponse(billService.create(id, billRequest), ResponseMessage.CREATE_BILL_SUCCESS.getMessage());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<BillDto>> update(@PathVariable Long id,
                                                          @Valid @RequestBody AddressRequest addressRequest,
                                                          BindingResult bindingResult) throws ValidationException, NotFoundException {
        HandleBindingResult.handle(bindingResult, addressRequest);
        return ResponseUtil.wrapResponse(billService.update(id, addressRequest), ResponseMessage.CREATE_BILL_SUCCESS.getMessage());
    }

    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<BillDto>>> getAll(@RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
                                                                        @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
                                                                        @RequestParam(name = "start") Date startAt,
                                                                        @RequestParam(name = "end") Date endAt) {
        return ResponseUtil.wrapResponse(billService.getAll(size, page, startAt, endAt), ResponseMessage.GET_ALL_BILLS_SUCCESS.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<BillDto>> get(@PathVariable Long id) throws NotFoundException {
        return ResponseUtil.wrapResponse(billService.get(id), ResponseMessage.GET_BILL_SUCCESS.getMessage());
    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<CommonResponse<Void>> markAsPaid(@PathVariable Long id) throws NotFoundException {
        billService.markAsPaid(id);
        return ResponseUtil.wrapResponse(null, ResponseMessage.PAID_SUCCESS.getMessage());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<CommonResponse<List<BillDto>>> getByUserId(@PathVariable Long id) throws ValidationException, NotFoundException {
        return ResponseUtil.wrapResponse(billService.getByUserId(id), ResponseMessage.GET_ALL_BILLS_SUCCESS.getMessage());
    }
}
