package com.example.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {
    @JsonProperty("image")
    private byte[] image;

    @JsonProperty("message")
    @Size(min = 1, message = "Message cannot be empty")
    @NotNull(message = "Message cannot be null")
    private String message;
}
