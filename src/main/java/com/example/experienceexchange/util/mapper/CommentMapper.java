package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getLastname() + \" \" + comment.getAuthor().getFirstname())")
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(target = "author", ignore = true)
    Comment commentDtoToComment(CommentDto commentDto);

    List<CommentDto> toCommentsDto(Collection<Comment> comments);
}