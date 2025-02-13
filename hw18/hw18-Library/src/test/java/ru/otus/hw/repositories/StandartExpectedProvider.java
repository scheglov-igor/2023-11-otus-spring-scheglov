package ru.otus.hw.repositories;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class StandartExpectedProvider {
    public static List<Author> getDbAuthors() {
        return LongStream.range(1, 4).boxed()
                .map(id -> new Author(id.toString(), "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return LongStream.range(1, 7).boxed()
                .map(id -> new Genre(id.toString(), "Genre_" + id))
                .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id.toString(),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }


    public static List<Comment> getDbComments(List<Book> dbBooks) {

        return List.of(
                new Comment("1", dbBooks.get(0), "comment_1"),
                new Comment("2", dbBooks.get(0), "comment_2"),
                new Comment("3", dbBooks.get(1), "comment_3")
        );
    }

    public static List<Comment> getDbComments() {

        var dbBooks = getDbBooks();

        return List.of(
                new Comment("1", dbBooks.get(0), "comment_1"),
                new Comment("2", dbBooks.get(0), "comment_2"),
                new Comment("3", dbBooks.get(1), "comment_3")
        );
    }


}
