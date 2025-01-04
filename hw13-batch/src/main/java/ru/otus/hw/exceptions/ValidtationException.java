package ru.otus.hw.exceptions;

import ru.otus.hw.pojo.ErrorMsg;

import java.util.List;

public class ValidtationException extends RuntimeException {

    private final List<ErrorMsg> errorMsgs;

    public ValidtationException(List<ErrorMsg> errorMsgs) {
        super(errorMsgs.toString());
        this.errorMsgs = errorMsgs;
    }

    public List<ErrorMsg> getErrorMsgs() {
        return errorMsgs;
    }

}