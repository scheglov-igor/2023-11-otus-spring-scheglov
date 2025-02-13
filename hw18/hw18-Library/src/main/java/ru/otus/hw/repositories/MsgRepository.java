package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Msg;

public interface MsgRepository extends MongoRepository<Msg, String> {}