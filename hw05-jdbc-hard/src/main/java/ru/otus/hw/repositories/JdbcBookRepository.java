package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        return jdbc.query(
                "select " +
                    "b.id as book_id, " +
                    "b.title as book_title, " +
                    "a.id as author_id, " +
                    "a.full_name as author_full_name, " +
                    "g.id as genre_id, " +
                    "g.name as genre_name " +
                    "from books b " +
                    "left join authors a on b.author_id = a.id " +
                    "left join books_genres bg on b.id = bg.book_id " +
                    "left join genres g on bg.genre_id = g.id " +
                    "where b.id = :id",
                Map.of("id", id),
                new BookResultSetExtractor()
        );
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Optional<Book> book = findById(id);
        if (book.isPresent()) {
            removeGenresRelationsFor(book.get());
            jdbc.update("delete from books where id = :id", Map.of("id", id));
        }
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query(
                "select " +
                    "b.id as book_id, " +
                    "b.title as book_title, " +
                    "a.id as author_id, " +
                    "a.full_name as author_full_name " +
                    "from books b " +
                    "left join authors a " +
                    "on b.author_id = a.id",
                new BookRowMapper()
        );
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query(
                "select book_id, genre_id from books_genres",
                (rs, i) -> new BookGenreRelation(
                        rs.getLong("book_id"),
                        rs.getLong("genre_id")
                ));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {

        Map<Long, Genre> genresMap = genres.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));

        booksWithoutGenres
                .forEach(book -> book.setGenres(
                        relations.stream()
                                .filter(bookGenreRelation -> bookGenreRelation.bookId() == book.getId())
                                .map(bookGenreRelation -> genresMap.get(bookGenreRelation.genreId))
                                .collect(Collectors.toList())
                ));

    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor().getId());

        jdbc.update("insert into books (title, author_id) values (:title, :authorId)",
                params, keyHolder, new String[]{"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {

        int updatedCount = jdbc.update("update books set title = :title, author_id = :authorId where id = :id",
                Map.of("title", book.getTitle(), "authorId", book.getAuthor().getId(), "id", book.getId()));

        if (updatedCount == 0) {
            throw new EntityNotFoundException("no book with id=%s".formatted(book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        List<BookGenreRelation> bookGenreRelationList = book.getGenres().stream()
                        .map(genre -> new BookGenreRelation(book.getId(), genre.getId()))
                        .collect(Collectors.toList());

        jdbc.batchUpdate("insert into books_genres(book_id, genre_id) values (:bookId, :genreId)",
                SqlParameterSourceUtils.createBatch(bookGenreRelationList));
    }

    private void removeGenresRelationsFor(Book book) {
        jdbc.update("delete from books_genres where book_id = :id", Map.of("id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("book_id");
            String title = rs.getString("book_title");
            long authorId = rs.getLong("author_id");
            String fullName = rs.getString("author_full_name");

            Author author = new Author(authorId, fullName);

            return new Book(id, title, author, new ArrayList<>());
        }
    }

    // ClassCanBeRecord
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Optional<Book>> {

        private final BookRowMapper rowMapper = new BookRowMapper();

        @Override
        public Optional<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {

            var bookMap = new HashMap<Long, Book>();

            while (rs.next()) {
                var book = bookMap.get(rs.getLong("id"));
                if (isNull(book)) {
                    book = rowMapper.mapRow(rs, rs.getRow());
                    bookMap.put(book.getId(), book);
                }
                var genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
                book.getGenres().add(genre);
            }

            List<Book> l = new ArrayList<>(bookMap.values());

            return isEmpty(bookMap) ? Optional.empty() : Optional.of(l.get(0));

        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
