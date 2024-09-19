package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(CommentDto commentDto) {
        return "Id: %s, CommentText: %s, Book: %s".formatted(
                commentDto.getId(), commentDto.getCommentText(), commentDto.getBook());
    }

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), bookConverter.toDto(comment.getBook()), comment.getCommentText());
    }

    public List<CommentDto> toDtoList(List<Comment> commentList) {
        return  commentList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Comment toDomainObject(CommentDto commentDto) {
        return new Comment(commentDto.getId(),
                bookConverter.toDomainObject(commentDto.getBook()), commentDto.getCommentText());
    }

}