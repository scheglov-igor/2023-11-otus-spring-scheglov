package ru.otus.hw.services;

import io.mongock.driver.mongodb.springdata.v4.config.SpringDataMongoV4Context;
import io.mongock.runner.springboot.EnableMongock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с жанрами")
@DataMongoTest
@EnableMongock
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({SpringDataMongoV4Context.class, GenreServiceImpl.class, GenreConverter.class})
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