package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreConverter {

    public String genreToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public List<GenreDto> toDtoList(List<Genre> genreList) {
        return  genreList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Genre toDomainObject(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getName());
    }

    public List<Genre> toDomainObjectList(List<GenreDto> genreDtoList) {
        return  genreDtoList.stream().map(this::toDomainObject).collect(Collectors.toList());
    }

}
