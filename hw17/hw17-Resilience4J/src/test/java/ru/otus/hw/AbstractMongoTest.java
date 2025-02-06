package ru.otus.hw;

import io.mongock.driver.mongodb.springdata.v4.config.SpringDataMongoV4Context;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

@DataMongoTest
@EnableMongock
@Import({SpringDataMongoV4Context.class})
public class AbstractMongoTest {
}
