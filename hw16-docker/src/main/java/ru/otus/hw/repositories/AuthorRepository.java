package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;

@Repository("Author")
@RepositoryRestResource(path = "author")
public interface AuthorRepository extends MongoRepository<Author, String> {
    List<Author> findAll();
}