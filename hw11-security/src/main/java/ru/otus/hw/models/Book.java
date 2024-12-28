package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "author")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Author author;

    @Field(name = "genres")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Genre> genres;

}