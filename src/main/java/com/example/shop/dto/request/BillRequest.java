package com.example.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillRequest {
    @JsonProperty("address")
    @NotNull
    private AddressRequest address;

    @JsonProperty("receiver_phone")
    private String phone;

    @JsonProperty("orders")
    @NotNull
    @NotEmpty
    private List<Long> details;
}
