package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long id);

    List<CommentDto> findByBookId(long bookId);

    CommentDto insertComment(long bookId, String commentText);

    CommentDto updateComment(long id, long bookId, String commentText);

    void deleteComment(long id);

}
