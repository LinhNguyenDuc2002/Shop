package com.example.shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    private Long id;

    @JsonProperty("specific_address")
    private String specificAddress;

    private String ward;

    private String district;

    private String city;

    private String country;
}
