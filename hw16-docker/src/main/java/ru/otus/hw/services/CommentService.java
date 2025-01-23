package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentFormDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    Optional<CommentFormDto> findFormDtoById(String id);

    List<CommentDto> findByBookId(String bookId);

    CommentDto insertComment(String bookId, String commentText);

    CommentDto updateComment(String id, String bookId, String commentText);

    CommentDto save(CommentFormDto comment);

    void deleteComment(String id);

}
