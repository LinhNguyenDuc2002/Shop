package com.example.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryRequest {
    @JsonProperty("name")
    @Size(min = 1, message = "Category name cannot be empty")
    @NotNull(message = "Category name cannot be null")
    private String name;

    @JsonProperty("note")
    private String note;
}
