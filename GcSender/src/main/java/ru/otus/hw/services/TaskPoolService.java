package ru.otus.hw.services;


import ru.otus.hw.models.TlgSendGcTb;

public interface TaskPoolService {

	String addTask(String rowid, TlgSendGcTb tlgSendGcTb);
	
	Long addTask(Long id, TlgSendGcTb tlgSendGcTb);

}
