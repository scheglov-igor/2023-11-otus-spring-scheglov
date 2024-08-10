package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами ")
@JdbcTest
@Import({JdbcAuthorRepository.class})
class JdbcAuthorRepositoryTest {

    @Autowired
    private JdbcAuthorRepository repositoryJdbc;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repositoryJdbc.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        expectedAuthors.forEach(System.out::println);
    }

    @DisplayName("должен загружать автора c id=3")
    @Test
    void shouldReturnCorrectAuthor() {
        var actualAuthor = repositoryJdbc.findById(3L);
        var expectedAuthor = new Author(3L, "Author_3");

        assertThat(actualAuthor.get()).isEqualTo(expectedAuthor);
    }


    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }



}