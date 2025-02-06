package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.model.BookData;

import java.time.Duration;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookInfoController {

    @GetMapping(value = "/additional-info")
    public BookData info(@RequestParam(name = "name") String name) throws InterruptedException {
        log.info("request. name:{} processing...", name);
        Integer importantValue = doJob();
        log.info("finish job for:{}", name);
        return new BookData(String.format("additional INFO for book: %s, importantValue: %s", name, importantValue));
    }

    private Integer doJob() throws InterruptedException {

        Random random = new Random();

        int duration = random.nextInt(10);
        log.info("job for {} seconds...", duration);
        Thread.sleep(Duration.ofSeconds(duration).toMillis());
        return duration;
    }

}