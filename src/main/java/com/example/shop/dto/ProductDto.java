package com.example.shop.dto;

import com.example.shop.entity.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long id;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("images")
    private List<String> images;

    @JsonProperty("quantity")
    private Long quantity;

    @JsonProperty("sold")
    private Long sold;

    @JsonProperty("update_day")
    private Date update_day;

    @JsonProperty("note")
    private String note;

    @JsonProperty("category")
    private CategoryDto category;

    @JsonProperty("comment")
    private Collection<CommentDto> comments;
}
