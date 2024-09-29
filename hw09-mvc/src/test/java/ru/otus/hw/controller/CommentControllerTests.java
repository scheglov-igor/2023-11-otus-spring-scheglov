package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentFormDto;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.StandartExpectedDtoProvider;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CommentController.class)
public class CommentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private CommentDto getCommentDto() {
        return new CommentDto("1", StandartExpectedDtoProvider.getBookDtoList().get(0), "Comment");
    }
    private CommentFormDto getCommentFormDto() {
        return new CommentFormDto("1", "1","Comment");
    }

    @Test
    public void testGetEditBook() throws Exception {

        given(commentService.findFormDtoById(any()))
                .willReturn(Optional.of(getCommentFormDto()));

        mockMvc.perform(get("/comments/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editcomment"))
                .andExpect(model().attributeExists("comment"));
    }

    @Test
    public void testException() throws Exception {

        given(commentService.findFormDtoById(any())).willReturn(Optional.empty());

        mockMvc.perform(get("/comments/edit/22"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"))
                .andExpect(model().attributeExists("exceptionName"))
                .andExpect(model().attributeExists("exceptionMessage"));
    }


    @Test
    public void testGetAddComment() throws Exception {

        mockMvc.perform(get("/comments/new?bookid=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editcomment"))
                .andExpect(model().attributeExists("comment"));
    }


    @Test
    void testSavComment() throws Exception {

        given(commentService.save(any()))
                .willReturn(getCommentDto());

        this.mockMvc
                .perform(post("/comments/save")
                    .param("id", "1")
                    .param("bookId", "1")
                    .param("commentText", "Comment")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books/1"));
    }

    @Test
    void testSaveCommentReturnValidation() throws Exception {

        given(commentService.save(any()))
                .willReturn(getCommentDto());

        this.mockMvc
                .perform(post("/comments/save")
                        .param("id", "")
                        .param("bookId", "")
                        .param("commentText", "")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("editcomment"))
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attributeHasErrors("comment"))
                .andExpect(model().attributeErrorCount("comment", 2));
    }

    @Test
    public void testDeleteCommentById() throws Exception {

        mockMvc.perform(get("/comments/delete/1?bookid=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/books/1"));
    }

}