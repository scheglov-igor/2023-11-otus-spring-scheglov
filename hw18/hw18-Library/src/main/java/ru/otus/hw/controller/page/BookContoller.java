package ru.otus.hw.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookContoller {

    private final BookService bookService;

    private final CommentService commentService;

    private final AuthorService authorService;

    private final GenreService genreService;


    @GetMapping("/")
    public String getDefault() {

        return "redirect:/books";
    }

    @GetMapping("/books")
    public String getBooks() {

        return "listbook";
    }




    @GetMapping("/books/{id}")
    public String findBookById(@PathVariable String id,
                               Model model) {

        var book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no id: %s".formatted(id)));
        model.addAttribute("book", book);
        model.addAttribute("comments", commentService.findByBookId(id));

        return "showbook";
    }

    @GetMapping("/books/new")
    public String addBook(Model model) {

        model.addAttribute("book", new BookFormDto(null, null, null, null));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());

        return "editbook";
    }

    @GetMapping("/books/edit/{id}")
    public String editBookById(@PathVariable(required = false) String id, Model model) {

        var book = bookService.findFormDtoById(id)
                .orElseThrow(() -> new EntityNotFoundException("no id: %s".formatted(id)));
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());

        return "editbook";
    }

}