package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.AbstractMongoTest;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями ")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({CommentServiceImpl.class, CommentConverter.class, BookConverter.class,
        AuthorConverter.class, GenreConverter.class})
class CommentServiceImplTest extends AbstractMongoTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @Autowired
    private CommentConverter commentConverter;

    private List<BookDto> bookDtoList;

    private List<CommentDto> commentDtoList;

    @BeforeEach
    void setUp() {
        bookDtoList = StandartExpectedDtoProvider.getBookDtoList();
        commentDtoList = StandartExpectedDtoProvider.getCommentDtoList();
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("ru.otus.hw.services.StandartExpectedDtoProvider#getCommentDtoList")
    void shouldReturnCorrectCommentById(CommentDto expectedComment) {
        var actualComment = commentServiceImpl.findById(expectedComment.getId());
        System.out.println("actualComment = " + actualComment);
        System.out.println("expectedComment = " + expectedComment);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentServiceImpl.findByBookId("1");
        var expectedComments = List.of(
                commentDtoList.get(0),
                commentDtoList.get(1));

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewComment() {
        var expectedComment = new CommentDto("4", bookDtoList.get(0), "comment_10500");
        var returnedComment = commentServiceImpl.insertComment("1", "comment_10500");
        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isEmpty())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("id")
                .isEqualTo(expectedComment);

        assertThat(Optional.ofNullable(mongoOperations.findById(returnedComment.getId(), Comment.class)))
                .isPresent()
                .map(comment -> commentConverter.toDto(comment))
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveUpdatedComment() {
        var expectedComment = new CommentDto("1", bookDtoList.get(0), "comment_100500");

        assertThat(commentServiceImpl.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = commentServiceImpl.updateComment(expectedComment.getId(),
                expectedComment.getBook().getId(), expectedComment.getCommentText());

        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(Optional.ofNullable(mongoOperations.findById(returnedComment.getId(), Comment.class)))
                .isPresent()
                .map(comment -> commentConverter.toDto(comment))
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteComment() {
        var existingComment = Optional.ofNullable(mongoOperations.findById("1", Comment.class));
        assertThat(existingComment).isPresent();
        commentServiceImpl.deleteComment(existingComment.get().getId());
        assertThat(commentServiceImpl.findById("1")).isEmpty();
        assertThat(Optional.ofNullable(mongoOperations.findById("1", Comment.class))).isEmpty();
    }

}