package com.example.shop.controller;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.AddressDto;
import com.example.shop.dto.request.AddressRequest;
import com.example.shop.dto.response.CommonResponse;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.service.AddressService;
import com.example.shop.util.HandleBindingResult;
import com.example.shop.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/users/{id}")
    public ResponseEntity<CommonResponse<AddressDto>> get(@PathVariable String id) throws NotFoundException, ValidationException {
        return ResponseUtil.wrapResponse(addressService.get(id), ResponseMessage.GET_ADDRESS_SUCCESS.getMessage());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<CommonResponse<AddressDto>> update(
            @PathVariable String id,
            @Valid @RequestBody AddressRequest addressRequest,
            BindingResult bindingResult) throws NotFoundException, ValidationException {
        HandleBindingResult.handle(bindingResult, addressRequest);
        return ResponseUtil.wrapResponse(addressService.update(addressRequest, id), ResponseMessage.UPDATE_ADDRESS_SUCCESS.getMessage());
    }
}
