package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.config.Config;
import ru.otus.hw.exception.ManualStopException;
import ru.otus.hw.models.GcSendingTb;
import ru.otus.hw.repositories.GcSendingTbRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GcSendingTbServiceImpl implements GcSendingTbService {

    private final GcSendingTbRepository gcSendingTbRepository;

    private final Config config;

    private final EnviromentService enviromentService;

    public Optional<GcSendingTb> selectGcSendingTb() {
        return gcSendingTbRepository.findById(0L);
    };


    @Override
    public void checkOtherGcSenders() throws ManualStopException {
        Optional<GcSendingTb> gcSendingTbOptional = selectGcSendingTb();

        if(gcSendingTbOptional.isEmpty()) {
            log.warn("No info about GcSender in gc_sending_tb");
            log.warn("SWITCH TO THIS GcSender");
            return;
        }

        GcSendingTb lastGcSendingTb = gcSendingTbOptional.get();

        // проверяем, есть ли активные сендеры
        Integer isActiveInt = lastGcSendingTb.getIsActive();
        if(isActiveInt != null && isActiveInt.equals(0)) {
            // если активных нет - возвращаемся
            log.warn("GcSender is not ACTIVE");
            log.warn("SWITCH TO THIS GcSender");
            return;
        }


        // если есть активный сендер - проверяем, когда последний раз обновлял информацию
        // дельта в мс
        //Long delta = LocalDateTime.now().toEpochSecond() - lastGcSendingTb.getDateChange().toEpochSecond();

        long deltaSeconds = ChronoUnit.SECONDS.between(lastGcSendingTb.getDateChange(), LocalDateTime.now());

        log.info("Last checking = {} sec ago on {}", deltaSeconds , lastGcSendingTb.getServerName());

        Integer senderScheduleSec = config.getSenderScheduleSec();

        // даем 10% на подумать.
        if (deltaSeconds > senderScheduleSec * 1.1) {
            // там что-то случилось, начинаем закачивать задесь.
            log.warn("Something wrong with gcSender on " + lastGcSendingTb.getServerName());
            log.warn("refresh time in config.properties (senderScheduleSec) = " + senderScheduleSec);
            log.warn("SGcSender is ACTIVE, last refresh was  " + deltaSeconds + " sec ago");
            log.warn("SWITCH TO THIS GcSender");
            return;
        }

        throw new ManualStopException("STOP! There is another GcSender running on: " + lastGcSendingTb.getServerName() + " last refresh was " + deltaSeconds + " sec ago");

    };

    @Override
    @Transactional
    public void startGcSender() {
        GcSendingTb gcSendingTb = fillGcSendingTb(true);
        gcSendingTbRepository.save(gcSendingTb);
    };

    @Override
    @Transactional
    public void stopGcSender() {
        GcSendingTb gcSendingTb = fillGcSendingTb(false);
        gcSendingTbRepository.save(gcSendingTb);
    };

    private GcSendingTb fillGcSendingTb(Boolean isActive) {
        GcSendingTb gcSendingTb = new GcSendingTb(
                0L,
                isActive ? 1 : 0,
                enviromentService.getHostInfo(),
                0,
                LocalDateTime.now()
        );
        return  gcSendingTb;

    }

    @Override
    @Transactional
    public GcSendingTb save(GcSendingTb gcSendingTb) {
        return gcSendingTbRepository.save(gcSendingTb);
    }
}
