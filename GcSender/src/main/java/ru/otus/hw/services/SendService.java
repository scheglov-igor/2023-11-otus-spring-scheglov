package ru.otus.hw.services;


import ru.otus.hw.models.TlgSendGcTb;

public interface SendService {

	String sendByRowid(String rowid, TlgSendGcTb tlgSendGcTb);
	Long sendById(Long id, TlgSendGcTb tlgSendGcTb);

}
