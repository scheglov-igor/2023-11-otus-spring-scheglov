package ru.otus.hw.migration;

import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.LibraryUser;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;

@ChangeUnit(id = "update-db-02-add-user", order = "002", author = "schiv")
@RequiredArgsConstructor
public class UpdateDb02AddUser {

    private final UserRepository userRepository;

    private final MongoOperations mongoTemplate;

    @BeforeExecution
    public void createIndex() {

        mongoTemplate.indexOps(LibraryUser.class)
                .ensureIndex(new Index()
                        .named("user_username_index")
                        .on("username", Sort.Direction.ASC)
                        .unique());
    }

    @RollbackBeforeExecution
    public void dropIndex() {
        mongoTemplate.indexOps(Book.class).dropIndex("user_username_index");
    }

    @Execution
    public void changeSet() {
        var users = userRepository.saveAll(getDbUsers());
    }

    private static List<LibraryUser> getDbUsers() {
        return List.of(
                new LibraryUser(
                        null,
                        "user",
                        "{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG",
                        List.of("USER"),
                        true)
        );
    }

    @RollbackExecution
    public void rollback() {
        userRepository.deleteAll();
    }
}