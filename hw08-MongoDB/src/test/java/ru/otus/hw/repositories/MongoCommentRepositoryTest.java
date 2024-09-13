package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.AbstractMongoTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с комментариями ")
@DataMongoTest
class MongoCommentRepositoryTest extends AbstractMongoTest {

    @Autowired
    private CommentRepository repository;

    //TODO Idea почему-то пишет ошибку на mongoOperations
    // Could not autowire. No beans of 'MongoOperations' type found.
    // но работает
    // в прошлом ДЗ на TestEntityManager тоже ругается...
    @Autowired
    private MongoOperations mongoOperations;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;
    private List<Comment> commentList;

    @BeforeEach
    void setUp() {
        dbAuthors = StandartExpectedProvider.getDbAuthors();
        dbGenres = StandartExpectedProvider.getDbGenres();
        dbBooks = StandartExpectedProvider.getDbBooks(dbAuthors, dbGenres);
        commentList = StandartExpectedProvider.getDbComments(dbBooks);
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("ru.otus.hw.repositories.StandartExpectedProvider#getDbComments")
    void shouldReturnCorrectCommentById(Comment expectedComment) {
        var actualComment = repository.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualComments = repository.findByBookId("1");
        var expectedComments = List.of(
                commentList.get(0),
                commentList.get(1));

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }


    @DisplayName("должен сохранять новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewComment() {
        var expectedComment = new Comment("4", dbBooks.get(0), "comment_10500");
        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(Optional.ofNullable(mongoOperations.findById(returnedComment.getId(), Comment.class)))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }


    @DisplayName("должен сохранять измененный комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment("1", dbBooks.get(0), "comment_100500");

        assertThat(
                Optional.ofNullable(mongoOperations.findById(expectedComment.getId(), Comment.class)))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = repository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(
                Optional.ofNullable(mongoOperations.findById(returnedComment.getId(), Comment.class)))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);


    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteComment() {
        var existingComment = repository.findById("1");
        assertThat(existingComment).isPresent();
        repository.delete(existingComment.get());
        assertThat(repository.findById("1")).isEmpty();
    }


}