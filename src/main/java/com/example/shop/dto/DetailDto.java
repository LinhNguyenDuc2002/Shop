package com.example.shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailDto {
    private Long id;

    private Long quantity;

    @JsonProperty("unit_price")
    private double unitPrice;

    private UserDto user;

    private ProductDto product;
}
