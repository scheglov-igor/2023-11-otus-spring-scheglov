package ru.otus.hw.exception;

public class ManualStopException extends Exception {
	public ManualStopException(String message) {
		super(message);
	}

	public ManualStopException(String message, Throwable cause) {
		super(message, cause);
	}

}