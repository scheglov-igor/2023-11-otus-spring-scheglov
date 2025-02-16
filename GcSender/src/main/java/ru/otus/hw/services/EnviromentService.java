package ru.otus.hw.services;


import ru.otus.hw.config.Config;
import ru.otus.hw.exception.CriticalStopException;

public interface EnviromentService {

	void showConfig();

	void showVersion();
	
	String getHostInfo();

	Config getConfig() throws CriticalStopException;

	int getJavaVersion();

}
