package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public class BookFormDto {

    private String id;

    @NotBlank(message = "{field-should-not-be-blank}")
    private String title;

    @NotBlank(message = "{field-should-not-be-blank}")
    private String authorId;

    @NotEmpty(message = "{field-should-not-be-blank}")
    private Set<String> genreIds;

}