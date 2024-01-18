package com.example.shop.util;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class HandleBindingResult {
    public static void handle(BindingResult bindingResult, Object object) throws ValidationException {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> map = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            log.error("The input of fields is not properly formatted");
            throw new ValidationException(object, ResponseMessage.INPUT_INVALID.getMessage());
        }
    }
}
