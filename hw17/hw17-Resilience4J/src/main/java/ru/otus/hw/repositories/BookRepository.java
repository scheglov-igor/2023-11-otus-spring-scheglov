package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;

@Repository("Book")
@RepositoryRestResource(path = "book")
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findAll();
}
