package com.example.shop.mapper;

import com.example.shop.dto.UserDto;
import com.example.shop.cache.TempUser;
import com.example.shop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Named("UserMapper")
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    @Mapping(target = "address", ignore = true)
    @Named("toDtoWithoutAddress")
    UserDto toDtoWithoutAddress(User user);

    UserDto toDto(TempUser tempUser);

    List<UserDto> toDtoList(List<User> users);
}
