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
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с авторами")
@DataMongoTest
@EnableMongock
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({SpringDataMongoV4Context.class, AuthorServiceImpl.class, AuthorConverter.class})
class AuthorServiceImplTest {

    @Autowired
    private AuthorServiceImpl authorServiceImpl;

    private List<AuthorDto> authorDtoList;

    @BeforeEach
    void setUp() {
        authorDtoList = StandartExpectedDtoProvider.getAuthorDtoList();
    }

    @Test
    void testFindAll() {
        var actualAuthors = authorServiceImpl.findAll();
        var expectedAuthors = authorDtoList;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        expectedAuthors.forEach(System.out::println);
    }

}