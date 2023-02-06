package com.app.shoppingzone.advice;

public class ObjectInvalidException extends RuntimeException {

	public ObjectInvalidException(String message) {
		super(message);
	}

	public ObjectInvalidException() {
	}
}
