package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Msg;
import ru.otus.hw.repositories.MsgRepository;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class MsgServiceImpl implements MsgService {
    private final MsgRepository msgRepository;

    @Override
    @Transactional(readOnly = true)
    public void generateAndSaveRandomMsgs(Integer count) {

        for (long i = 0; i < count; i++) {
            Msg msg = new Msg(null, i, generateString());
            System.out.println("msgDto = " + msg);
            msgRepository.save(msg);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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



