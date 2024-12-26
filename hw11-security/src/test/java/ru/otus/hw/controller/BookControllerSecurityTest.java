package ru.otus.hw.controller;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.controller.page.BookContoller;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.stream.Stream;

@WebMvcTest(controllers = {BookContoller.class})
public class BookControllerSecurityTest extends AbstractControllersSecurityTest{

    @MockBean
    private BookService bookService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    public static Stream<Arguments> getSecurityTestData() {
        var roles = new String[] {"USER"};
        return Stream.of(
                Arguments.of("get", "/books", null, null, null, 302, true),
                Arguments.of("get", "/books", null, "user", roles, 200, false),
                Arguments.of("get", "/books/1", null, null, null, 302, true),
                Arguments.of("get", "/books/1", null, "user", roles, 200, false),
                Arguments.of("get", "/books/new", null, null, null, 302, true),
                Arguments.of("get", "/books/new", null, "user", roles, 200, false),
                Arguments.of("get", "/books/edit/1", null, null, null, 302, true),
                Arguments.of("get", "/books/edit/1", null, "user", roles, 200, false)
        );
    }
}
