package com.as.crichub.exception;

public class DecryptionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DecryptionException(String message) {
		super(message);
	}

	public DecryptionException(String message, Throwable cause) {
		super(message, cause);
	}
}
