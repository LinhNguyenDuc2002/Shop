package com.example.shop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
