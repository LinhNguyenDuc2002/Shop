package com.example.shop.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class TokenRefreshException extends RuntimeException{
    private HttpStatus status;

    private String message;
}
