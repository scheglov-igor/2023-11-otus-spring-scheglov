package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentFormDto;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.StandartExpectedDtoProvider;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST-контроллер для работы с комментариями")
@WebMvcTest(controllers = CommentRestController.class,
        excludeAutoConfiguration = {SecurityConfig.class, SecurityAutoConfiguration.class})
public class CommentRestControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private CommentDto getCommentDto() {
        return new CommentDto("1", StandartExpectedDtoProvider.getBookDtoList().get(0), "Comment");
    }
    private CommentFormDto getCommentFormDto() {
        return new CommentFormDto("1", "1","Comment");
    }


    @Test
    public void getCommentByIdTest() throws Exception {

        given(commentService.findById(any())).willReturn(Optional.of(getCommentDto()));

        mvc.perform(get("/api/comment/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getCommentDto())));
    }


    @Test
    public void getCommentByBookTest() throws Exception {

        given(commentService.findByBookId(any())).willReturn(List.of(getCommentDto()));

        mvc.perform(get("/api/comment").param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(getCommentDto()))));
    }

}
