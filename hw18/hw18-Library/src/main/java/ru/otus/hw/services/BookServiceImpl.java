package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.client.BookAdditionalInfoClient;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookData;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    private final BookAdditionalInfoClient bookAdditionalInfoClient;

    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(String id) {
        var book = bookRepository.findById(id);
        var bookDto = book.map(bookConverter::toDto);
        bookDto = requestAdditionalInfo(bookDto);
        return bookDto;
    }

    private Optional<BookDto> requestAdditionalInfo(Optional<BookDto> bookDto) {
        if (bookDto.isPresent()) {
            var startTime = System.currentTimeMillis();
            String additionalInfo = circuitBreakerFactory.create("defaultCircuitBreaker").run(() -> {
                log.info("request additional info...");
                ResponseEntity<BookData> addressResponse = bookAdditionalInfoClient
                        .getAdditionalInfo(bookDto.get().getTitle());
                log.info("addressResponse.getBody() = {}", addressResponse.getBody());
                BookData bookData = addressResponse.getBody();
                log.info("bookData {}", bookData);
                return bookData.data();
            }, throwable -> {
                log.error("delay call failed error:{}", throwable.getMessage());
                return null;
            });
            log.info("duration {} ms", System.currentTimeMillis() - startTime);
            var requestResult = String.format(
                    "ClientInfo name:%s, additional:%s", bookDto.get().getTitle(), additionalInfo);
            log.info("requestResult {}", requestResult);
            bookDto.get().setAdditionalInfo(additionalInfo);
        }
        return bookDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookFormDto> findFormDtoById(String id) {
        var book = bookRepository.findById(id);
        return book.map(bookConverter::toFormDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        var bookList = bookRepository.findAll();
        return bookConverter.toDtoList(bookList);
    }


    @Override
    @Transactional(readOnly = true)
    public Boolean isLibraryEmpty() {
        var bookList = bookRepository.count();
        return bookList == 0L;
    }

    @Override
    @Transactional
    @Secured({ "ROLE_ADMIN" })
    public BookDto insert(String title, String authorId, Set<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    @Transactional
    @Secured({ "ROLE_ADMIN" })
    public BookDto update(String id, String title, String authorId, Set<String> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Override
    @Transactional
    @Secured({ "ROLE_ADMIN" })
    public BookDto insert(BookFormDto bookFormDto) {
        return save(null, bookFormDto.getTitle(), bookFormDto.getAuthorId(), bookFormDto.getGenreIds());
    }

    @Override
    @Transactional
    @Secured({ "ROLE_ADMIN" })
    public BookDto update(BookFormDto bookFormDto) {
        return save(bookFormDto.getId(), bookFormDto.getTitle(), bookFormDto.getAuthorId(), bookFormDto.getGenreIds());
    }

    @Override
    @Transactional
    @Secured({ "ROLE_ADMIN" })
    public void deleteById(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Book id must not be null");
        }
        commentRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }

    private BookDto save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        try {
            var  book = bookRepository.save(new Book(id, title, author, genres));
            return bookConverter.toDto(book);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("Duplicate book title: %s".formatted(title), e);
        }
    }



}
