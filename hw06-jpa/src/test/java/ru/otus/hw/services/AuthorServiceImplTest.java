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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.JpaAuthorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с авторами")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({AuthorServiceImpl.class, JpaAuthorRepository.class, AuthorConverter.class})
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