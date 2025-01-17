package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.AbstractMongoTest;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.PaperBookDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Сервис для работы с книгами")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({BookServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
class BookServiceImplTest extends AbstractMongoTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @Autowired
    private BookConverter bookConverter;

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
        System.out.println("############ shouldReturnCorrectBookById");
        var actualBook = bookServiceImpl.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }


    @MockBean
    private BookPrinterGateway bookPrinterGateway;

    @DisplayName("должен загружать бумажную книгу по id")
    @ParameterizedTest
    @MethodSource("ru.otus.hw.services.StandartExpectedDtoProvider#getPaperBookDtoList")
    void shouldReturnCorrectPaperBookById(PaperBookDto expectedBook) {

        System.out.println("############ shouldReturnCorrectPaperBookById");

        given(bookPrinterGateway.process(expectedBook.getId())).willReturn(Optional.of(expectedBook));

        var actualBook = bookServiceImpl.printBook(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать книгу (FormDto) по id")
    @ParameterizedTest
    @MethodSource("ru.otus.hw.services.StandartExpectedDtoProvider#getBookFormDtoList")
    void shouldReturnCorrectFormDtoBookById(BookFormDto expectedBook) {
        System.out.println("############ shouldReturnCorrectBookById");
        var actualBook = bookServiceImpl.findFormDtoById(expectedBook.getId());
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
        var expectedBook = new BookDto("4", "BookTitle_10500", authorDtoList.get(0), List.of(genreDtoList.get(0), genreDtoList.get(2)));
        var returnedBook = bookServiceImpl.insert(expectedBook.getTitle(), expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()));
        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isEmpty())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("id")
                .isEqualTo(expectedBook);

        assertThat(Optional.ofNullable(mongoOperations.findById(returnedBook.getId(), Book.class)))
                .isPresent()
                .map(comment -> bookConverter.toDto(comment))
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять новую книгу (BookFormDto)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBookFormDto() {
        var newBook = new BookFormDto("", "BookTitle_10500", String.valueOf(1), Set.of(String.valueOf(1), String.valueOf(3)));
        var expectedBook = new BookDto("4", "BookTitle_10500", authorDtoList.get(0), List.of(genreDtoList.get(0), genreDtoList.get(2)));

        var returnedBook = bookServiceImpl.insert(newBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isEmpty())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("id")
                .isEqualTo(expectedBook);

        assertThat(Optional.ofNullable(mongoOperations.findById(returnedBook.getId(), Book.class)))
                .isPresent()
                .map(comment -> bookConverter.toDto(comment))
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveUpdatedBook() {
        var expectedBook = new BookDto("1", "BookTitle_10500", authorDtoList.get(2),
                List.of(genreDtoList.get(4), genreDtoList.get(5)));

        assertThat(Optional.ofNullable(mongoOperations.findById(expectedBook.getId(), Book.class)))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookServiceImpl.update(
                expectedBook.getId(), expectedBook.getTitle(), expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );

        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(Optional.ofNullable(mongoOperations.findById(returnedBook.getId(), Book.class)))
                .isPresent()
                .map(comment -> bookConverter.toDto(comment))
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу (BookFormDto)")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveUpdatedBookFormDto() {
        var bookFormDto = new BookFormDto("1", "BookTitle_10500", String.valueOf(3), Set.of(String.valueOf(5), String.valueOf(6)));
        var expectedBook = new BookDto("1", "BookTitle_10500", authorDtoList.get(2),
                List.of(genreDtoList.get(4), genreDtoList.get(5)));

        assertThat(Optional.ofNullable(mongoOperations.findById(expectedBook.getId(), Book.class)))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookServiceImpl.update(bookFormDto);

        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(Optional.ofNullable(mongoOperations.findById(returnedBook.getId(), Book.class)))
                .isPresent()
                .map(comment -> bookConverter.toDto(comment))
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        var existingBook = Optional.ofNullable(mongoOperations.findById("1", Book.class));
        assertThat(existingBook).isPresent();
        bookServiceImpl.deleteById(existingBook.get().getId());
        assertThat(Optional.ofNullable(mongoOperations.findById(existingBook.get().getId(), Book.class)))
                .isEmpty();
    }




}