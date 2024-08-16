package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
@Import({JpaGenreRepository.class, GenreConverter.class})
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository repository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
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
        var actualGenres = repository.findAllByIds(Set.of(1L, 3L));
        var expectedGenres = IntStream.of(1, 3)
                .boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        expectedGenres.forEach(System.out::println);
    }


    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }



}