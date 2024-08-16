package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    // cbid 1
    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    // cbb 1
    @ShellMethod(value = "Find comments by book id", key = "cbb")
    public String findCommentByBookId(long id) {
        return commentService.findByBookId(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cins 1 new_comment
    @ShellMethod(value = "Comment insert", key = "cins")
    public String createComment(Long bookId, String commentText) {
        var commentDto = commentService.insertComment(bookId, commentText);
        return commentConverter.commentToString(commentDto);
    }

    // cupd 1 2 changed_comment
    @ShellMethod(value = "Comment update", key = "cupd")
    public String updateComment(Long id, Long bookId, String commentText) {
        var commentDto = commentService.updateComment(id, bookId, commentText);
        return commentConverter.commentToString(commentDto);
    }

    // cdel 1
    @ShellMethod(value = "Comment delete", key = "cdel")
    public void deleteComment(Long id) {
        commentService.deleteComment(id);
    }

}
