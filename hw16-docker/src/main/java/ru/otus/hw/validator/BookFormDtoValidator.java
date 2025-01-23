package ru.otus.hw.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.otus.hw.exceptions.ValidtationException;
import ru.otus.hw.pojo.ErrorMsg;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookFormDtoValidator {

    public void validateBookDto(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ErrorMsg> errorMsgs = new ArrayList<>(bindingResult.getErrorCount());
            for (FieldError fieldWithError : bindingResult.getFieldErrors()) {
                errorMsgs.add(new ErrorMsg(
                        fieldWithError.getField(),
                        fieldWithError.getCode(),
                        fieldWithError.getDefaultMessage()));
            }
            throw new ValidtationException(errorMsgs);
        }
    }
}
