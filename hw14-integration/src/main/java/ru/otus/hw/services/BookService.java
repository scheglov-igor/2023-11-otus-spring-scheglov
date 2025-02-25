package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.PaperBookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDto> findById(String id);

    Optional<BookFormDto> findFormDtoById(String id);

    List<BookDto> findAll();

    BookDto insert(String title, String authorId, Set<String> genresIds);

    BookDto update(String id, String title, String authorId, Set<String> genresIds);

    BookDto insert(BookFormDto bookRequestPojo);

    BookDto update(BookFormDto bookRequestPojo);

    void deleteById(String id);

    Optional<PaperBookDto> printBook(String id);
}
