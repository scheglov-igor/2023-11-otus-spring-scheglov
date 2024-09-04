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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с авторами ")
@DataMongoTest
@EnableMongock
@Import({SpringDataMongoV4Context.class})
//TODO единственный способ, которым смог запустить миграцию
// https://github.com/mongock/mongock/issues/551
class MongoAuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = StandartExpectedProvider.getDbAuthors();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repository.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        expectedAuthors.forEach(System.out::println);
    }

    @DisplayName("должен загружать автора c id=3")
    @Test
    void shouldReturnCorrectAuthor() {
        var actualAuthor = repository.findById("3");
        var expectedAuthor = new Author("3", "Author_3");

        assertThat(actualAuthor.orElse(null)).isEqualTo(expectedAuthor);
    }


}