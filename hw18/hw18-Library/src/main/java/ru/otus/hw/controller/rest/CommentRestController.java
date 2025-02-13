package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @GetMapping("/api/comment/{id}")
    public CommentDto getCommentById(@PathVariable String id) {

        var commentDto = commentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no id: %s".formatted(id)));

        return commentDto;
    }

    @GetMapping("/api/comment")
    public List<CommentDto> getCommentsByBookId(@RequestParam(name = "bookId") String bookId) {

        var comments = commentService.findByBookId(bookId);

        return comments;
    }

}
