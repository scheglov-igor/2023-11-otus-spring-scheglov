package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.User;

@Repository("User")
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String name);
}