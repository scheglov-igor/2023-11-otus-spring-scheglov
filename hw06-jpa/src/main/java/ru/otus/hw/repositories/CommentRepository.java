package ru.otus.hw.repositories;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(Long id);

    List<Comment> findByBookId(Long bookId);

    Comment save(Comment comment);

    void delete(Comment comment);
}