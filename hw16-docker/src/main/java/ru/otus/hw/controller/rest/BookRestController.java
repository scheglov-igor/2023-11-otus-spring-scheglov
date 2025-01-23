package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.validator.BookFormDtoValidator;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookFormDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    private final BookFormDtoValidator bookFormDtoValidator;

    @GetMapping("/api/book")
    public List<BookDto> getAllBooks() {

        var bookDtoList = bookService.findAll();

        return bookDtoList;
    }

    @GetMapping("/api/book/{id}")
    public BookDto getBookById(@PathVariable String id) {

        var book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no id: %s".formatted(id)));

        return book;
    }

    @PostMapping("/api/book")
    public BookDto createNewBook(
            @Valid @RequestBody BookFormDto book,
            BindingResult bindingResult) {

        bookFormDtoValidator.validateBookDto(bindingResult);

        var savedBook = bookService.insert(book);
        return savedBook;
    }


    @PutMapping("/api/book")
    public BookDto editExistingBook(
            @Valid @RequestBody BookFormDto book,
            BindingResult bindingResult) {

        bookFormDtoValidator.validateBookDto(bindingResult);

        var savedBook = bookService.update(book);
        return savedBook;
    }


    @DeleteMapping("/api/book/{id}")
    public void deleteBookById(@PathVariable String id) {
        bookService.deleteById(id);
    }

}
