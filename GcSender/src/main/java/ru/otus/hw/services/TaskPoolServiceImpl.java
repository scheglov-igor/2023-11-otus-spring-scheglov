package ru.otus.hw.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.TlgSendGcTb;

@Slf4j
@Service
public class TaskPoolServiceImpl implements TaskPoolService {

	private final SendService sendService;	

	// Пул потоков.
	private final ExecutorService executor;
	//TODO переделать на TaskExecutor ????
	//	this.executor = Executors.newSingleThreadExecutor();
	private final QueueService queueService;

	public TaskPoolServiceImpl(SendService sendService,  QueueService queueService) {
		this.sendService = sendService;
		this.executor = Executors.newSingleThreadExecutor();
		this.queueService = queueService;
	}

	// просто прием задач
	// проверяем, что такого rowid нет в Set отправляемых
	// и складываем их в очередь на отправку
	@Override
	public String addTask(String rowid, TlgSendGcTb tlgSendGcTb) {
		// throws InterruptedException, ExecutionException

		log.info("addTask rowId={}, tlgSendGcTb={}", rowid, tlgSendGcTb);
		
		boolean needProcess = queueService.addRowid(rowid);
		
		if(needProcess) {
			Future<String> resultInFuture1 = executor.submit(() -> sendService.sendByRowid(rowid, tlgSendGcTb));
			log.info("task submitted {}", resultInFuture1);
		}
		else {
			log.info("row {} already in queue", rowid);
		}	
			
        return rowid;
	}
	
	// просто прием задач
	// проверяем, что такого rowid нет в Set отправляемых
	// и складываем их в очередь на отправку
	@Override
	public Long addTask(Long id, TlgSendGcTb tlgSendGcTb) {

		log.info("addTask: id={}, tlgSendGcTb={}", id, tlgSendGcTb, tlgSendGcTb);

		boolean needProcess = queueService.addId(id);
		
		if(needProcess) {
			Future<Long> resultInFuture1 = executor.submit(() -> sendService.sendById(id, tlgSendGcTb));
			log.info("task submitted for execution");
		}
		else {
			log.info("id {} already in queue", id);
		}	
			
        return id;
	}
	
}
