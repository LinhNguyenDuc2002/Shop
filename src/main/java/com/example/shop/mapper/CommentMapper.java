package com.example.shop.mapper;

import com.example.shop.dto.CommentDto;
import com.example.shop.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Named("CommentMapper")
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = UserMapper.class)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "user", qualifiedByName = {"UserMapper", "toDtoWithoutAddress"})
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtoList(List<Comment> comments);
}
