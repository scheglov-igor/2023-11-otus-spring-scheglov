package ru.otus.hw.services;

import ru.otus.hw.exception.CriticalStopException;

import java.sql.SQLException;


public interface ListenerService {

	void startListener() throws CriticalStopException;

	void unregisterAllDatabaseChangeNotification() throws SQLException, CriticalStopException;
	
}
