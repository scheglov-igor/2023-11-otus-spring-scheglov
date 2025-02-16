package ru.otus.hw.commandlinerunners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.exception.CriticalStopException;
import ru.otus.hw.exception.ManualStopException;
import ru.otus.hw.services.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class Startup implements CommandLineRunner {

    private final EnviromentService enviromentService;

    private final SendAllService sendAllService;

    private final GcSendingTbService gcSendingTbService;

    private final ListenerService listenerService;

    @Override
    public void run(String... args) {

        enviromentService.showVersion();

        log.info("JavaVersion {}", enviromentService.getJavaVersion());

        //TODO как бы это заставить работать с кривым конфигом, когда приложение не может стартовать?
        if((args.length>0) && args[0] != null && !args[0].isEmpty() && args[0].toUpperCase().contains("SHOWCONFIG")) {
            enviromentService.showConfig();
            return;
        }


//
//        List<TlgSendGcTb> l = tlgSendGcTbService.selectAllByStatus();
//        System.out.println("l = " + l.size());
//        System.out.println("l = " + l);
//        DbService dbService = new DbServiceImpl(enviromentService);

//        System.out.println("config.getPostgresVersion() = " + config.getPostgresVersion());

//        System.out.println("config = " + config);

//        testRunnerService.run();

//
//        List<GcSendXmlTb> gcSendXmlTbList = gcSendXmlTbRepository.findAll();
//        System.out.println("gcSendXmlTbList = " + gcSendXmlTbList.size());
//        for (GcSendXmlTb gcSendXmlTb: gcSendXmlTbList) {
//            System.out.println("gcSendXmlTb = " + gcSendXmlTb);
//        }
//
//
//        List<TlgSendGcTb> tlgSendGcTbList = tlgSendGcTbRepository.findAll();
//        System.out.println("tlgSendGcTbList = " + tlgSendGcTbList.size());
//        for (TlgSendGcTb tlgSendGcTb: tlgSendGcTbList) {
//            System.out.println("tlgSendGcTb = " + tlgSendGcTb);
//        }
//

        log.info("check other GcSenders...");

        try {
            gcSendingTbService.checkOtherGcSenders();
            log.info("starting...");

            // отправляем все записи, которые не отправлены на данный момент
            // проверяем сразу при запуске,
            // Если упали при первом запуске - останавливаем приложение CriticalStopException
            sendAllService.sendAllByStatus();

            // запускаем слежение за таблицей
            listenerService.startListener();

            // вешаем хук на завершение работы - закрыть все DBChangeNotification
//            Runtime.getRuntime().addShutdownHook(shutdownHook);

            // проверяем раз в 5 минут, что всё отправлено
            sendAllService.scheduleSendAllByStatus();


        } catch (ManualStopException e) {
            log.warn("ManualStop: {}", e.getMessage());
            System.exit(0);
        } catch (CriticalStopException e) {
            log.error("CriticalStopException: {}", e);
            System.exit(0);
        }
    }
}
