package com.example.shop.util;

import com.example.shop.dto.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseUtil<T> {
    public static <T> ResponseEntity<CommonResponse<T>> wrapResponse(T data, String message) {
        CommonResponse<T> response = new CommonResponse<>();

        if(data != null) {
            response.setData(data);
        }

        response.setMessage(message);
        return ResponseEntity.ok(response);
    }
}
