package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;

@Repository("Author")
public interface AuthorRepository extends MongoRepository<Author, String> {
    List<Author> findAll();
}