package com.example.shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailDto {
    private Long quantity;

    private UserDto user;

    private ProductDto product;
}
