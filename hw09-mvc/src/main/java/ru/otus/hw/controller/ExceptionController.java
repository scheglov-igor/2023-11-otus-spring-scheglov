package ru.otus.hw.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.hw.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value	= {EntityNotFoundException.class})
    protected String handleConflict(RuntimeException ex, WebRequest request, Model model) {
        model.addAttribute("exceptionName", "Entety not found");
        model.addAttribute("exceptionMessage", ex.getMessage());

        return "exception";
    }
}