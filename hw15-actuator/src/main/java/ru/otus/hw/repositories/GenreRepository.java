package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@Repository("Genre")
@RepositoryRestResource(path = "genre")
public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAll();

    @RestResource(path = "ids", rel = "ids")
    List<Genre> findByIdIn(Set<String> ids);
}
