package ru.otus.hw.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class BookDto {

    private final String id;

    private final  String title;

    private final AuthorDto author;

    private final List<GenreDto> genres;

    private String additionalInfo;

}