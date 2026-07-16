package com.as.crichub.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

	 private final HttpStatus status;
	    private final String message;
	    private final int code;
	 
	    public ErrorResponse(HttpStatus status, String message, int code) {
	        this.status = status;
	        this.message = message;
			this.code = code;
	    }
	 
	    public HttpStatus getStatus() {
	        return status;
	    }
	 
	    public String getMessage() {
	        return message;
	    }
	 
		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}
}
