package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthorDto {

    private Long id;

    private String fullName;
}