package ru.otus.hw.controller.page;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.CommentFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments/edit/{id}")
    public String editCommentById(@PathVariable(required = false) String id, Model model) {

        var comment = commentService.findFormDtoById(id)
                .orElseThrow(() -> new EntityNotFoundException("no id: %s".formatted(id)));

        model.addAttribute("comment", comment);

        return "editcomment";
    }


    @GetMapping("/comments/new")
    public String addComment(@RequestParam (name = "bookid") String bookId, Model model) {

        model.addAttribute("comment", new CommentFormDto(null, bookId, null));

        return "editcomment";
    }

    @PostMapping("/comments/save")
    public String saveComment(@Valid @ModelAttribute("comment") CommentFormDto comment,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "editcomment";
        }

        commentService.save(comment);

        return "redirect:/books/%s".formatted(comment.getBookId());
    }

    @PostMapping("/comments/delete/{id}")
    public String deleteCommentById(@PathVariable(required = false) String id,
                                    @RequestParam(name = "bookid") String bookId) {

        commentService.deleteComment(id);

        return "redirect:/books/%s".formatted(bookId);
    }

}