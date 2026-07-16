package com.as.crichub.exception;

public class DataAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataAlreadyExistsException(String message) {
		super(message);
	}

	public DataAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
