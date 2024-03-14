package com.example.shop.converter;

import com.example.shop.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Converter
@Slf4j
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {
    @Override
    public String convertToDatabaseColumn(RoleType roleType) {
        return roleType == null ? RoleType.CUSTOMER.name() : roleType.name();
    }

    @Override
    public RoleType convertToEntityAttribute(String s) {
        if(!StringUtils.hasText(s)) {
            return RoleType.CUSTOMER;
        }
        try {
            return RoleType.valueOf(s);
        } catch (Exception e) {
            log.warn("Invalid role type: {}", s);
            return RoleType.CUSTOMER;
        }
    }
}
