package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({BookServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookServiceImpl;

    private List<AuthorDto> authorDtoList;

    private List<GenreDto> genreDtoList;

    private List<BookDto> bookDtoList;

    @BeforeEach
    void setUp() {
        authorDtoList = StandartExpectedDtoProvider.getAuthorDtoList();
        genreDtoList = StandartExpectedDtoProvider.getGenreDtoList();
        bookDtoList = StandartExpectedDtoProvider.getBookDtoList(authorDtoList, genreDtoList);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("ru.otus.hw.services.StandartExpectedDtoProvider#getBookDtoList")
    void shouldReturnCorrectBookById(BookDto expectedBook) {
        var actualBook = bookServiceImpl.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookServiceImpl.findAll();
        var expectedBooks = bookDtoList;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBook() {
        var expectedBook = new BookDto(4L, "BookTitle_10500", authorDtoList.get(0), List.of(genreDtoList.get(0), genreDtoList.get(2)));
        var returnedBook = bookServiceImpl.insert(expectedBook.getTitle(), expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()));
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookServiceImpl.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveUpdatedBook() {
        var expectedBook = new BookDto(1L, "BookTitle_10500", authorDtoList.get(2),
                List.of(genreDtoList.get(4), genreDtoList.get(5)));

        assertThat(bookServiceImpl.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookServiceImpl.update(
                expectedBook.getId(), expectedBook.getTitle(), expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookServiceImpl.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        var existingBook = bookServiceImpl.findById(1L);
        assertThat(existingBook).isPresent();
        bookServiceImpl.deleteById(existingBook.get().getId());
        assertThat(bookServiceImpl.findById(1L)).isEmpty();
    }

}