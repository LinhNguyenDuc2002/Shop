package com.example.shop.mapper;

import com.example.shop.dto.AddressDto;
import com.example.shop.dto.request.AddressRequest;
import com.example.shop.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressDto toDto (Address address);

    Address toEntity(AddressRequest addressRequest);

    @Mapping(target = "id", ignore = true)
    void mapAddressRequestToAddress(AddressRequest addressRequest, @MappingTarget Address address);
}
