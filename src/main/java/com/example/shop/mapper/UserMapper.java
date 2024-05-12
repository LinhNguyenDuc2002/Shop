package com.example.shop.mapper;

import com.example.shop.dto.UserDto;
import com.example.shop.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> {
    @Override
    public Class<UserDto> getDtoClass() {
        return UserDto.class;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }


}
