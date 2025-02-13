package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDto author) {
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
    }

    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public List<AuthorDto> toDtoList(List<Author> authorList) {
        return  authorList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Author toDomainObject(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getFullName());
    }

}
