package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentDto {

    private Long id;

    private BookDto book;

    private String commentText;

}