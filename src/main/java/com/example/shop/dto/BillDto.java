package com.example.shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("purchase_date")
    private Date purchaseDate;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("user")
    private UserDto user;

    @JsonProperty("address")
    private AddressDto address;

    @JsonProperty("orders")
    private List<DetailDto> details;
}
