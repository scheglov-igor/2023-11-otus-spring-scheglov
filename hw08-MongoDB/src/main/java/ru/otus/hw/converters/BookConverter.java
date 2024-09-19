package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(BookDto book) {
        var booksString = book.getGenres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                booksString);
    }

    public BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                authorConverter.toDto(book.getAuthor()), genreConverter.toDtoList(book.getGenres()));
    }

    public List<BookDto> toDtoList(List<Book> bookList) {
        return  bookList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Book toDomainObject(BookDto bookDto) {
        return new Book(bookDto.getId(), bookDto.getTitle(),
                authorConverter.toDomainObject(bookDto.getAuthor()),
                genreConverter.toDomainObjectList(bookDto.getGenres()));
    }
}
