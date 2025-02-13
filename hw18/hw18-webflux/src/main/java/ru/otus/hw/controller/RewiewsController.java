package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.model.Msg;
import ru.otus.hw.repository.MsgRepository;

import java.time.Duration;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin()

public class RewiewsController {

    private final MsgRepository msgRepository;

    @GetMapping(value = "data", produces = APPLICATION_NDJSON_VALUE)
    public Flux<Msg> ndjson() {
        System.out.println("/data");
        Flux<Msg> msgFlux = msgRepository.findAll().delayElements(Duration.ofSeconds(1));
//        Flux<Msg> msgFlux = successful();

        return msgFlux;
    }


    private Flux<Msg> successful() {

        return Flux.interval(Duration.ofSeconds(1))
              //  .take(5)
                .doOnEach(longSignal -> {
                    System.out.println("longSignal = " + longSignal);
                })
                .map(i -> new Msg(null, i, generateString()));
    }


    private String generateString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println(generatedString);

        return generatedString;
    }
}