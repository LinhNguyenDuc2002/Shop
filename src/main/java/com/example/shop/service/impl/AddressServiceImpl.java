package com.example.shop.service.impl;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.UserDto;
import com.example.shop.dto.request.AddressRequest;
import com.example.shop.entity.Address;
import com.example.shop.entity.User;
import com.example.shop.exception.NotFoundException;
import com.example.shop.mapper.AddressMapper;
import com.example.shop.mapper.UserMapper;
import com.example.shop.repository.AddressRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public UserDto updateAddress(AddressRequest addressRequest, Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(()->{
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
        });

        if(user.getAddress() == null) {
            Address address = AddressMapper.INSTANCE.toEntity(addressRequest);
            user.setAddress(address);
            userRepository.save(user);
        }
        else {
            Address address = user.getAddress();

            AddressMapper.INSTANCE.mapAddressRequestToAddress(addressRequest, address);
            addressRepository.save(address);
        }

        return UserMapper.INSTANCE.toDto(user);
    }
}
