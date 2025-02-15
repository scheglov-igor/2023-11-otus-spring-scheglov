package ru.otus.hw.services;

public interface QueueService {

	boolean addRowid(String rowid);
	boolean removeRowid(String rowid);
	
	boolean addId(Long id);
	boolean removeId(Long id);

}
