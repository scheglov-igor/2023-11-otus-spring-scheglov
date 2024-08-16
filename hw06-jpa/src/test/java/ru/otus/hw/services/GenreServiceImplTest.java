package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Сервис для работы с жанрами")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({GenreServiceImpl.class, JpaGenreRepository.class, GenreConverter.class})
class GenreServiceImplTest {

    @Autowired
    private GenreServiceImpl genreServiceImpl;

    private List<GenreDto> genreDtoList;

    @BeforeEach
    void setUp() {
        genreDtoList = StandartExpectedDtoProvider.getGenreDtoList();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectAllGenresList() {
        var actualGenres = genreServiceImpl.findAll();
        var expectedGenres = genreDtoList;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        expectedGenres.forEach(System.out::println);
    }

}