package com.example.shop.mapper;

import com.example.shop.dto.AddressDto;
import com.example.shop.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper extends AbstractMapper<Address, AddressDto> {
    @Override
    public Class<AddressDto> getDtoClass() {
        return AddressDto.class;
    }

    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }
}
