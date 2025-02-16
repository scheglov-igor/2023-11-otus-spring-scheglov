package ru.otus.hw.commandlinerunners;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.GcSendingTbService;
import ru.otus.hw.services.ListenerService;

@Component
@Slf4j
@RequiredArgsConstructor
public class Shutdown {

	private final ListenerService listenerService;

	private final GcSendingTbService gcSendingTbService;

	@PreDestroy
	public void destroy() {

		log.info("Shutdown activated!");

		try {
			log.info("unregister... " + " " + Thread.currentThread());
			listenerService.unregisterAllDatabaseChangeNotification();
		} catch (Exception e) {
			log.error("Exception on ShutdownHook run", e);
		}

		try {
			gcSendingTbService.stopGcSender();
		} catch (Exception e) {
			log.error("Exception on ShutdownHook run", e);
		}

		log.info("Shutdown finished!");

	}

}