package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;

@Repository("Comment")
@RepositoryRestResource(path = "comment")
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAll();

    @RestResource(path = "bookId", rel = "bookId")
    List<Comment> findByBookId(String bookId);

    void deleteByBookId(String bookId);
}