package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;

@Repository("Book")
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findAll();
}
