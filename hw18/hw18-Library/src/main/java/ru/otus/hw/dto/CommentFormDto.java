package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentFormDto {

    private String id;

    @NotBlank(message = "{field-should-not-be-blank}")
    private String bookId;

    @NotBlank(message = "{field-should-not-be-blank}")
    private String commentText;

}