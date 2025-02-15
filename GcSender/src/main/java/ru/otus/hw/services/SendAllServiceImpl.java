package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.Config;
import ru.otus.hw.exception.CriticalStopException;
import ru.otus.hw.models.TlgSendGcTb;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendAllServiceImpl implements SendAllService{
	
	private final EnviromentService enviromentService;
	private final TaskPoolService taskPoolService;
	private final GcSendingTbService gcSendingTbService;
	private final TlgSendGcTbService tlgSendGcTbService;
	private final Config config;



	@Scheduled(fixedRateString="${senderScheduleSec}", timeUnit = TimeUnit.SECONDS)
	public void work() {
		// task execution logic
		try {
			sendAllByStatus();
		} catch (CriticalStopException e) {
			log.error("CriticalStopException on scheduleSendAllByStatus", e);
		}
	}

	@Override
	public void scheduleSendAllByStatus() {

		Integer scheduleRateSec = config.getSenderScheduleSec();
		log.info("Start schedule. Checking every {} sec", scheduleRateSec);
		
	    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	    executor.scheduleAtFixedRate(() -> {
			try {
				sendAllByStatus();
			} catch (CriticalStopException e) {
				log.error("CriticalStopException on scheduleSendAllByStatus", e);
			}
			
		}, scheduleRateSec, scheduleRateSec, TimeUnit.SECONDS);
	}
	
	// Добавляем в очередь все подходящие записи, которые появились, пока не следили или если пропустили
	@Override
	public void sendAllByStatus() throws CriticalStopException {

		gcSendingTbService.startGcSender();
		List<TlgSendGcTb> tlgSendGcTbList = tlgSendGcTbService.selectAllByStatus();

		Boolean isPostgres = config.getPostgresVersion();

		for (TlgSendGcTb tlgSendGcTb : tlgSendGcTbList) {
			if(isPostgres) {
				taskPoolService.addTask(tlgSendGcTb.getId(), tlgSendGcTb);
			} else {
				taskPoolService.addTask(tlgSendGcTb.getRowid(), tlgSendGcTb);
			}
		}

	}
	
}
