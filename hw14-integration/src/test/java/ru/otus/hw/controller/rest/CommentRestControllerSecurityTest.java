package ru.otus.hw.controller.rest;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.controller.AbstractControllersSecurityTest;
import ru.otus.hw.services.CommentService;

import java.util.stream.Stream;

@WebMvcTest(controllers = {CommentRestController.class})
public class CommentRestControllerSecurityTest extends AbstractControllersSecurityTest {

    @MockBean
    private CommentService commentService;

    public static Stream<Arguments> getSecurityTestData() {
        System.out.println("Impementation of ABSTRACT!!!!");
        var roleUser = new String[]{"USER"};
        var roleAdmin = new String[] {"ADMIN"};
        return Stream.of(
                Arguments.of("get", "/api/comment/1", null, null, null, 302, true),
                Arguments.of("get", "/api/comment/1", null, "user", roleUser, 200, false),
                Arguments.of("get", "/api/comment/1", null, "admin", roleAdmin, 200, false),
                Arguments.of("get", "/api/comment?bookId=1", null, null, null, 302, true),
                Arguments.of("get", "/api/comment?bookId=1", null, "user", roleUser, 200, false),
                Arguments.of("get", "/api/comment?bookId=1", null, "admin", roleAdmin, 200, false)
        );
    }
}
