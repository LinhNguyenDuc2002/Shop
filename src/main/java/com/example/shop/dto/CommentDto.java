package com.example.shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private Long id;

    @JsonProperty("comment-date")
    private Date commentDate;

    private String image;

    private String message;

    @JsonProperty("user")
    private UserDto user;
}
