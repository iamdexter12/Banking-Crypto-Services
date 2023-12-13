package com.fintechharbor.notification.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends GenericException {

	private static final long serialVersionUID = 1L;

	private static final HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

	public NotFoundException(final String fieldName, final String message) {
		super(fieldName, httpStatusCode, String.format("%s", message));
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
