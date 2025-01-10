package ru.otus.hw.migration;

import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@ChangeUnit(id = "init-all", order = "001", author = "schiv")
@RequiredArgsConstructor
public class InitDbChangeUnit {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final MongoOperations mongoTemplate;

    @BeforeExecution
    public void createIndex() {

        mongoTemplate.indexOps(Author.class)
                .ensureIndex(new Index()
                        .named("author_fullName_index")
                        .on("fullName", Sort.Direction.ASC)
                        .unique());

        mongoTemplate.indexOps(Genre.class)
                .ensureIndex(new Index()
                        .named("genre_name_index")
                        .on("name", Sort.Direction.ASC)
                        .unique());

        mongoTemplate.indexOps(Book.class)
                .ensureIndex(new Index()
                        .named("book_title_index")
                        .on("title", Sort.Direction.ASC)
                        .unique());
    }

    @RollbackBeforeExecution
    public void dropIndex() {
        mongoTemplate.indexOps(Author.class).dropIndex("author_fullName_index");
        mongoTemplate.indexOps(Genre.class).dropIndex("genre_name_index");
        mongoTemplate.indexOps(Book.class).dropIndex("book_title_index");
    }

    @Execution
    public void changeSet() {
        var authors = authorRepository.saveAll(getDbAuthors());
        var genres = genreRepository.saveAll(getDbGenres());
        var books = bookRepository.saveAll(getDbBooks(authors, genres));
        var comments = commentRepository.saveAll(getDbComments(books));
    }

    private static List<Author> getDbAuthors() {
        return LongStream.range(1, 4000).boxed()
                .map(id -> new Author(id + "s", "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return LongStream.range(1, 9000).boxed()
                .map(id -> new Genre(id + "s", "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4000).boxed()
                .map(id -> new Book(id + "s",
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Comment> getDbComments(List<Book> books) {
        return IntStream.range(1, 4000).boxed()
                .map(id -> new Comment(
                        id + "s",
                        books.get(id / 2),
                        "comment_" + id
                ))
                .toList();
    }

    @RollbackExecution
    public void rollback() {
        authorRepository.deleteAll();
        genreRepository.deleteAll();
        bookRepository.deleteAll();
        commentRepository.deleteAll();
    }
}