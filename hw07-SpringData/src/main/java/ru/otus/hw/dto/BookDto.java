package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BookDto {

    private Long id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

}