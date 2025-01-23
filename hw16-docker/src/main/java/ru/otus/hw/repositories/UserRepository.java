package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.LibraryUser;

@Repository("User")
@RepositoryRestResource(path = "user")
public interface UserRepository extends MongoRepository<LibraryUser, String> {

    @RestResource(path = "username", rel = "username")
    LibraryUser findByUsername(String name);
}