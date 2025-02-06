package ru.otus.hw.controller;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.controller.page.CommentController;
import ru.otus.hw.services.CommentService;

import java.util.stream.Stream;

@WebMvcTest(controllers = {CommentController.class})
public class CommentControllerSecurityTest extends AbstractControllersSecurityTest{

    @MockBean
    private CommentService commentService;

    public static Stream<Arguments> getSecurityTestData() {
        System.out.println("Impementation of ABSTRACT!!!!");
        var roleUser = new String[]{"USER"};
        var roleAdmin = new String[] {"ADMIN"};
        return Stream.of(
                Arguments.of("get", "/comments/edit/1", null, null, null, 302, true),
                Arguments.of("get", "/comments/edit/1", null, "user", roleUser, 200, false),
                Arguments.of("get", "/comments/edit/1", null, "admin", roleAdmin, 200, false),
                Arguments.of("get", "/comments/new?bookid=1", null, null, null, 302, true),
                Arguments.of("get", "/comments/new?bookid=1", null, "user", roleUser, 200, false),
                Arguments.of("get", "/comments/new?bookid=1", null, "admin", roleAdmin, 200, false),
                Arguments.of("post", "/comments/save", null, null, null, 302, true),
                Arguments.of("post", "/comments/save", null, "user", roleUser, 200, false),
                Arguments.of("post", "/comments/save", null, "admin", roleAdmin, 200, false),
                Arguments.of("post", "/comments/delete/1?bookid=1", null, null, null, 302, true),
                Arguments.of("post", "/comments/delete/1?bookid=1", null, "user", roleUser, 302, false),
                Arguments.of("post", "/comments/delete/1?bookid=1", null, "admin", roleAdmin, 302, false)
        );
    }
}
