package ru.otus.hw.repositories;

import io.mongock.driver.mongodb.springdata.v4.config.SpringDataMongoV4Context;
import io.mongock.runner.springboot.EnableMongock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с книгами ")
@DataMongoTest
@EnableMongock
@Import({SpringDataMongoV4Context.class})
class MongoBookRepositoryTest {

    @Autowired
    private BookRepository repository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = StandartExpectedProvider.getDbAuthors();
        dbGenres = StandartExpectedProvider.getDbGenres();
        dbBooks = StandartExpectedProvider.getDbBooks(dbAuthors, dbGenres);
    }


    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

}