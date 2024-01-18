package com.example.shop.mapper;

import com.example.shop.dto.ProductDto;
import com.example.shop.entity.Image;
import com.example.shop.entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Named("ProductMapper")
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = CategoryMapper.class)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "category", qualifiedByName = {"CategoryMapper", "toDtoWithoutProducts"})
    @Mapping(target = "images", source = "images", qualifiedByName = "customImage")
    @Named("toDto")
    ProductDto toDto(Product product);

    @IterableMapping(qualifiedByName = "toDto") //use toDto
    List<ProductDto> toDtoList(List<Product> product);

    @Named("customImage")
    default List<String> customImage(Collection<Image> images) {
        if(images != null && !images.isEmpty()) {
            List<String> imageUrl = images.stream().map(image -> image.getUrl()).toList();
            return imageUrl;
        }

        return List.of();
    }
}
