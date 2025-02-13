package ru.otus.hw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorMsg {

    private String fieldName;

    private String errorCode;

    private String defaultMessage;

}
