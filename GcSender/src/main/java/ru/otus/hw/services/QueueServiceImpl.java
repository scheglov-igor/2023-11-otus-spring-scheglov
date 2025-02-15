package ru.otus.hw.services;

import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueueServiceImpl implements QueueService {
	
	////////////////////////
	//RowidSet
	////////////////////////

	public QueueServiceImpl() {
		super();

		this.queueRowidToSend = new HashSet<String>();
		this.queueIdToSend = new HashSet<>();
	}
	
	
	// ORACLE
	
	// PARAMS
	//TODO эта штука должна быть атомик или synchronized. 
	// если одновременно будет чтение и удаление - может возникнуть проблема
	private Set<String> queueRowidToSend;
	
	@Override
	public boolean addRowid(String rowid) {
		boolean added = queueRowidToSend.add(rowid);
		log.info("add ROWID={} succes={}", rowid, added);

		log.info("rowidToSend: " + queueRowidToSend);
		log.info("idToSend: " + queueIdToSend);
		return added;
	} 
	
	@Override
	public boolean removeRowid(String rowid) {
		boolean removed = queueRowidToSend.remove(rowid);
		log.info("remove ROWID={} succes removed={}", rowid, removed);

		log.info("rowidToSend: " + queueRowidToSend);
		log.info("idToSend: " + queueIdToSend);
		return removed;
	}
	
	

	// POSTGRES
	

	// PARAMS
	//TODO эта штука должна быть атомик или synchronized. 
	// если одновременно будет чтение и удаление - может возникнуть проблема
	private Set<Long> queueIdToSend;

	
	@Override
	public boolean addId(Long id) {
		boolean added = queueIdToSend.add(id);
		log.info("adding ID={}... succes={}", id, added);

		log.info("rowidToSend: " + queueRowidToSend);
		log.info("idToSend: " + queueIdToSend);
		return added;
	} 
	
	@Override
	public boolean removeId(Long id) {
		boolean removed = queueIdToSend.remove(id);
		log.info("remove ID={} succes={}", id, removed);

		log.info("queueRowidToSend: " + queueRowidToSend);
		log.info("queueIdToSend: " + queueIdToSend);
		
		return removed;
	}
		

	
}
