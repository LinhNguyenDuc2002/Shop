package com.example.shop.mapper;

import com.example.shop.dto.DetailDto;
import com.example.shop.entity.Detail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Named("DetailMapper")
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProductMapper.class, UserMapper.class})
public interface DetailMapper {
    DetailMapper INSTANCE = Mappers.getMapper(DetailMapper.class);

    @Mapping(target = "user", qualifiedByName = {"UserMapper", "toDtoWithoutAddress"})
    @Mapping(target = "product", qualifiedByName = {"ProductMapper", "toDto"})
    DetailDto toDto(Detail detail);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", qualifiedByName = {"ProductMapper", "toDto"})
    @Named("toDtoWithoutUser")
    DetailDto toDtoWithoutUser(Detail detail);

    List<DetailDto> toDtoList(List<Detail> detail);
}
