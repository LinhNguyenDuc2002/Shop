package com.example.shop.mapper;

import com.example.shop.dto.CategoryDto;
import com.example.shop.entity.Category;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Named("CategoryMapper")
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "products", ignore = true)
    @Named("toDtoWithoutProducts")
    CategoryDto toDto(Category category);

    CategoryDto toDtoDetail(Category category);

    @IterableMapping(qualifiedByName = "toDtoWithoutProducts") //use toDto
    List<CategoryDto> toCategoryDtoList(List<Category> categories);
}
