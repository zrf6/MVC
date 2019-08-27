package com.mvc.exception;
 
 
 
public class MVCException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public MVCException() {
		super();
	}

	public MVCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MVCException(String message, Throwable cause) {
		super(message, cause);
	}

	public MVCException(String message) {
		super(message);
	}

	public MVCException(Throwable cause) {
		super(cause);
	}
   
}

