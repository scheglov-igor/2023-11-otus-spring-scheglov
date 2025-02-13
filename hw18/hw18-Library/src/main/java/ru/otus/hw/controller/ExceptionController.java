package ru.otus.hw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.ValidtationException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value	= {EntityNotFoundException.class})
    protected String handleConflict(RuntimeException ex, WebRequest request, Model model) {
        model.addAttribute("exceptionName", "Entety not found");
        model.addAttribute("exceptionMessage", ex.getMessage());

        return "exception";
    }


    @ExceptionHandler(value	= {ValidtationException.class})
    public  ResponseEntity handleValidationException(ValidtationException ex) {
        return new ResponseEntity<>(ex.getErrorMsgs(), HttpStatus.BAD_REQUEST);
    }

}