package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class StandartExpectedDtoProvider {

    public static List<AuthorDto> getAuthorDtoList() {
        return LongStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id.toString(), "Author_" + id))
                .toList();
    }

    public static List<GenreDto> getGenreDtoList() {
        return LongStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id.toString(), "Genre_" + id))
                .toList();
    }

    public static List<BookDto> getBookDtoList() {
        var authorDtoList = getAuthorDtoList();
        var genreDtoList = getGenreDtoList();
        return getBookDtoList(authorDtoList, genreDtoList);
    }

    public static List<BookDto> getBookDtoList(List<AuthorDto> authorDtoList, List<GenreDto> genreDtoList) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id.toString(),
                        "BookTitle_" + id,
                        authorDtoList.get(id - 1),
                        genreDtoList.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<CommentDto> getCommentDtoList() {

        var bookDtoList = StandartExpectedDtoProvider.getBookDtoList();
        return List.of(
                new CommentDto("1", bookDtoList.get(0), "comment_1"),
                new CommentDto("2", bookDtoList.get(0), "comment_2"),
                new CommentDto("3", bookDtoList.get(1), "comment_3")
        );
    }

    public static List<CommentDto> getCommentDtoList(List<BookDto> bookDtoList) {

        return List.of(
                new CommentDto("1", bookDtoList.get(0), "comment_1"),
                new CommentDto("2", bookDtoList.get(0), "comment_2"),
                new CommentDto("3", bookDtoList.get(3), "comment_3")
        );
    }

}
