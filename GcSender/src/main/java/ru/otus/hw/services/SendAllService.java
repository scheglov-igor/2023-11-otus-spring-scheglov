package ru.otus.hw.services;


import ru.otus.hw.exception.CriticalStopException;

public interface SendAllService {

	void sendAllByStatus() throws CriticalStopException;

	void scheduleSendAllByStatus();

}
