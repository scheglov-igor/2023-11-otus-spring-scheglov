package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.LibraryUser;

@Repository("User")
public interface UserRepository extends MongoRepository<LibraryUser, String> {
    LibraryUser findByUsername(String name);
}