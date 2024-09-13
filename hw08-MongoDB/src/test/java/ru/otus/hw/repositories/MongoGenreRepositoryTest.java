package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.AbstractMongoTest;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с жанрами ")
class MongoGenreRepositoryTest extends AbstractMongoTest {

    @Autowired
    private GenreRepository repository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = StandartExpectedProvider.getDbGenres();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectAllGenresList() {
        var actualGenres = repository.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        expectedGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать список жанров c id=1 и id=3")
    @Test
    void shouldReturnCorrectSelectedGenresList() {
        var actualGenres = repository.findByIdIn(Set.of("1", "3"));
        var expectedGenres = LongStream.of(1, 3)
                .boxed()
                .map(id -> new Genre(id.toString(), "Genre_" + id))
                .toList();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        expectedGenres.forEach(System.out::println);
    }

}