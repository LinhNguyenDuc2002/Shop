package com.example.shop.service.impl;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.AddressDto;
import com.example.shop.dto.request.AddressRequest;
import com.example.shop.entity.Address;
import com.example.shop.entity.User;
import com.example.shop.exception.NotFoundException;
import com.example.shop.mapper.AddressMapper;
import com.example.shop.repository.AddressRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public AddressDto update(AddressRequest addressRequest, String id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        if (user.getAddress() == null) {
            Address address = convertToAddress(addressRequest);
            user.setAddress(address);
            userRepository.save(user);
        } else {
            Address address = user.getAddress();

            if (StringUtils.hasText(addressRequest.getCity())) {
                address.setCity(address.getCity());
            }
            if (StringUtils.hasText(addressRequest.getDistrict())) {
                address.setDistrict(addressRequest.getDistrict());
            }
            if (StringUtils.hasText(addressRequest.getWard())) {
                address.setWard(addressRequest.getWard());
            }
            if (StringUtils.hasText(addressRequest.getCountry())) {
                address.setCountry(addressRequest.getCountry());
            }
            if (StringUtils.hasText(addressRequest.getSpecificAddress())) {
                address.setDetail(addressRequest.getSpecificAddress());
            }
            addressRepository.save(address);
        }

        return addressMapper.toDto(user.getAddress());
    }

    @Override
    public AddressDto get(String id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        return addressMapper.toDto(user.getAddress());
    }

    private Address convertToAddress(AddressRequest addressRequest) {
        if (addressRequest == null) {
            return new Address();
        }

        return Address.builder()
                .city(addressRequest.getCity())
                .country(addressRequest.getCountry())
                .district(addressRequest.getDistrict())
                .ward(addressRequest.getWard())
                .detail(addressRequest.getSpecificAddress())
                .build();
    }
}
