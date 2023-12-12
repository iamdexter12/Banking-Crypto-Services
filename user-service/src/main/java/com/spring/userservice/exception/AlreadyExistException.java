package com.spring.userservice.exception;

import org.springframework.http.HttpStatus;


public class AlreadyExistException extends GenericException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus httpStatusCode = HttpStatus.CONFLICT;

	public AlreadyExistException(final String fieldName, final String message) {
		super(fieldName, httpStatusCode, String.format("%s", message));
	}
}
