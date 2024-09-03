package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    private String id;

    //TODO В маппинге реляционных БД принято жестко прописывать названия полей @Column(name= "full_name")
    // так как структура таблиц меняется реже, чем классы
    // что будет хорошим тоном для nosql?
    // как я понял, что тут наоборот правильно оставить как можно более гибкую структуру
    // поэтому @Field(name = "full_name") не нужно
    private String fullName;
}