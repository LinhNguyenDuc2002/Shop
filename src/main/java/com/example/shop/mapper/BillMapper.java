package com.example.shop.mapper;

import com.example.shop.dto.BillDto;
import com.example.shop.entity.Bill;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AddressMapper.class, DetailMapper.class, UserMapper.class})
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    @Mapping(target = "details", qualifiedByName = {"DetailMapper", "toDtoWithoutUser"})
    @Mapping(target = "user", qualifiedByName = {"UserMapper", "toDtoWithoutAddress"})
    BillDto toDto(Bill bill);

    @Mapping(target = "details", qualifiedByName = {"DetailMapper", "toDtoWithoutUser"})
    @Mapping(target = "user", ignore = true)
    @Named("toDtoWithoutUser")
    BillDto toDtoWithoutUser(Bill bill);

    @IterableMapping(qualifiedByName = "toDtoWithoutUser")
    List<BillDto> toDtoList(List<Bill> bills);
}
