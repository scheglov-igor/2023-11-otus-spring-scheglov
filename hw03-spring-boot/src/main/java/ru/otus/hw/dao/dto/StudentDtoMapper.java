package ru.otus.hw.dao.dto;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Student;

@Component
public class StudentDtoMapper {
    public StudentDto toDto(Student student) {
        return new StudentDto(student.firstName(), student.lastName());
    }
}
