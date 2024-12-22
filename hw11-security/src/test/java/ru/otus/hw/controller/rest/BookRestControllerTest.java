package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.validator.BookFormDtoValidator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("REST-контроллер для работы с книгами")
@WebMvcTest(BookRestController.class)
@Import({BookFormDtoValidator.class, SecurityConfig.class})
public class BookRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    private BookDto getBook() {
        return new BookDto("1", "TestTitle", new AuthorDto("1", "AuthorName"),
                List.of(new GenreDto("1", "Genre")));
    }

    private List<BookDto> getBooks() {
        return List.of(getBook());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testGetAllBooks() throws Exception {

        given(bookService.findAll()).willReturn(getBooks());

        mvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getBooks())));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testGetBookById() throws Exception {

        given(bookService.findById("1")).willReturn(Optional.of(getBook()));

        mvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getBook())));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testCreateNewBook() throws Exception {

        BookFormDto bookFormDto = new BookFormDto(null, "TestTitle", "1",Set.of("1"));

        String expectedContent = mapper.writeValueAsString(bookFormDto);

        given(bookService.insert(any())).willReturn(getBook());

        String expectedResult = mapper.writeValueAsString(getBook());

        mvc.perform(post("/api/book").with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(expectedContent))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));

    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testEditExistingBook() throws Exception {

        BookFormDto bookFormDto = new BookFormDto("1", "TestTitle", "1",Set.of("1"));

        String expectedContent = mapper.writeValueAsString(bookFormDto);

        given(bookService.update(any())).willReturn(getBook());

        String expectedResult = mapper.writeValueAsString(getBook());

        mvc.perform(put("/api/book").with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(expectedContent))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));

    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testDeleteBookById() throws Exception {
        mvc.perform(delete("/api/book/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testSecurityException() throws Exception {
        mvc.perform(get("/api/book/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testSecurityDeleteException() throws Exception {
        mvc.perform(delete("/api/book/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
