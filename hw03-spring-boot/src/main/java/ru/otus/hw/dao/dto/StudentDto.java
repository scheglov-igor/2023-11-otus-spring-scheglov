package ru.otus.hw.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDto {
    private String firstName;

    private String lastName;

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
