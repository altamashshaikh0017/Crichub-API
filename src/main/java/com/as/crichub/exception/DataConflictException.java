package com.as.crichub.exception;

public class DataConflictException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataConflictException(String message) {
		super(message);
	}

	public DataConflictException(String message, Throwable cause) {
		super(message, cause);
	}
}
