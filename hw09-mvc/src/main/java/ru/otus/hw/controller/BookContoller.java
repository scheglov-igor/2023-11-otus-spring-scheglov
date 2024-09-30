package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String findAllBooks(Model model) {

        var bookDtoList = bookService.findAll();
        model.addAttribute("bookDtoList", bookDtoList);

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

    @PostMapping("/books/save")
    public String saveBook(@Valid @ModelAttribute("book") BookFormDto book,
                           BindingResult bindingResult,
                           ModelMap model) {

        //TODO не нашел способ, чтобы не нужно было передавать обратно авторов и книги.
        // кажется, это как-то некрасиво
        // а без этого - не работает.
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "editbook";
        }

        var savedBook = bookService.save(book);

        return "redirect:/books/%s".formatted(savedBook.getId());
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBookById(@PathVariable(required = false) String id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}