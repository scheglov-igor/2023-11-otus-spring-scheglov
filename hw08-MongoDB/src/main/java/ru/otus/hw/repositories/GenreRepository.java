package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@Repository("Genre")
public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAll();

    List<Genre> findByIdIn(Set<String> ids);
}
