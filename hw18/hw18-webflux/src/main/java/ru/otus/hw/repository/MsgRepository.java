package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.Msg;

@Repository
public interface MsgRepository extends ReactiveMongoRepository<Msg, String> {

}