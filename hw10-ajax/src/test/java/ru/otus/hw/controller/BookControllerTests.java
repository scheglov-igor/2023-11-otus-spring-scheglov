package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.page.BookContoller;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookContoller.class)
public class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    private BookDto getBook() {
        return new BookDto("1", "TestTitle", new AuthorDto("1", "AuthorName"),
                List.of(new GenreDto("1", "Genre")));
    }
    private BookFormDto getBookFormDto() {
        return new BookFormDto("1", "TestTitle","1", Set.of("1"));
    }

    @Test
    public void testFindAllBooks() throws Exception {

        given(bookService.findAll())
                .willReturn(List.of(getBook()));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("listbook"));
    }

    @Test
    public void testFindBookById() throws Exception {

        given(bookService.findById(any()))
                .willReturn(Optional.of(getBook()));

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("showbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    public void testException() throws Exception {
        given(bookService.findFormDtoById(any())).willReturn(Optional.empty());

        mockMvc.perform(get("/books/22"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"))
                .andExpect(model().attributeExists("exceptionName"))
                .andExpect(model().attributeExists("exceptionMessage"));
    }

    @Test
    public void testGetAddBook() throws Exception {

        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    public void testGetEditBook() throws Exception {

        given(bookService.findFormDtoById(any()))
                .willReturn(Optional.of(getBookFormDto()));

        mockMvc.perform(get("/books/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editbook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

}