package com.example.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRequest {
    @JsonProperty("product-name")
    @Size(min = 1, message = "Product name cannot be empty")
    @NotNull(message = "Product name cannot be null")
    private String productName;

    @JsonProperty("price")
    @Min(value = 0, message = "The price must be greater than or equal to 0")
    @NotNull(message = "Price cannot be null")
    private Double price;

    @JsonProperty("quantity")
    @Min(value = 0, message = "The quantity must be greater than or equal to 0")
    @NotNull(message = "Quantity cannot be null")
    private Long quantity;

    @JsonProperty("category")
    @NotNull(message = "Category cannot be null")
    private Long category;

    @JsonProperty("note")
    private String note;
}
