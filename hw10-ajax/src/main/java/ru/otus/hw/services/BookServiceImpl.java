package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(String id) {
        var book = bookRepository.findById(id);
        return book.map(bookConverter::toDto);
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
    @Transactional
    public BookDto insert(String title, String authorId, Set<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    @Transactional
    public BookDto update(String id, String title, String authorId, Set<String> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Override
    @Transactional
    public BookDto insert(BookFormDto bookFormDto) {
        return save(null, bookFormDto.getTitle(), bookFormDto.getAuthorId(), bookFormDto.getGenreIds());
    }

    @Override
    @Transactional
    public BookDto update(BookFormDto bookFormDto) {
        return save(bookFormDto.getId(), bookFormDto.getTitle(), bookFormDto.getAuthorId(), bookFormDto.getGenreIds());
    }

    @Override
    @Transactional
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
