package ru.otus.hw.exception;

public class NeedRepeatException extends Exception {

	public NeedRepeatException(Throwable cause) {
		super(cause);
	}

	public NeedRepeatException(String message) {
		super(message);
	}

	public NeedRepeatException(String message, Throwable cause) {
		super(message, cause);
	}

}