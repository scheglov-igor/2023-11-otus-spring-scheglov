package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    List<CommentDto> findByBookId(String bookId);

    CommentDto insertComment(String bookId, String commentText);

    CommentDto updateComment(String id, String bookId, String commentText);

    void deleteComment(String id);

}
