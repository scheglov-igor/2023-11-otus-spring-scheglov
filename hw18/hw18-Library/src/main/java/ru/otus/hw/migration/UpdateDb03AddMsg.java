package ru.otus.hw.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Msg;
import ru.otus.hw.repositories.MsgRepository;

import java.util.List;

@ChangeUnit(id = "update-db-03-add-msg", order = "003", author = "schiv")
@RequiredArgsConstructor
public class UpdateDb03AddMsg {

    private final MsgRepository msgRepository;

    private final MongoOperations mongoTemplate;

    @Execution
    public void changeSet() {
        var msgs = msgRepository.saveAll(getDbMsgs());
    }

    private static List<Msg> getDbMsgs() {
        return List.of(
                new Msg(
                        null,
                        1L,
                        "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG"),
                new Msg(
                        null,
                        2L,
                        "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
        );
    }

    @RollbackExecution
    public void rollback() {
        msgRepository.deleteAll();
    }
}