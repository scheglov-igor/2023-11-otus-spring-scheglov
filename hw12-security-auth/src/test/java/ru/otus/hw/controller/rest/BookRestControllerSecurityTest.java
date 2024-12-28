package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controller.AbstractControllersSecurityTest;
import ru.otus.hw.services.BookService;
import ru.otus.hw.validator.BookFormDtoValidator;

import java.util.stream.Stream;

@WebMvcTest(controllers = {BookRestController.class})
@Import({BookFormDtoValidator.class, SecurityConfig.class})
public class BookRestControllerSecurityTest extends AbstractControllersSecurityTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    public static Stream<Arguments> getSecurityTestData() {
        System.out.println("Impementation of ABSTRACT!!!!");

        var roles = new String[]{"USER"};

        String content = "{\"id\":null,\"title\":\"TestTitle\",\"authorId\":\"1\",\"genreIds\":[\"1\"]}";

        return Stream.of(
                Arguments.of("get", "/api/book", null, null, null, 302, true),
                Arguments.of("get", "/api/book", null, "user", roles, 200, false),
                Arguments.of("get", "/api/book/1", null, null, null, 302, true),
                Arguments.of("get", "/api/book/1", null, "user", roles, 200, false),
                Arguments.of("post", "/api/book", content, null, null, 302, true),
                Arguments.of("post", "/api/book", content, "user", roles, 200, false),
                Arguments.of("put", "/api/book", content, null, null, 302, true),
                Arguments.of("put", "/api/book", content, "user", roles, 200, false),
                Arguments.of("delete", "/api/book/1", null, null, null, 302, true),
                Arguments.of("delete", "/api/book/1", null, "user", roles, 200, false)
        );
    }
}