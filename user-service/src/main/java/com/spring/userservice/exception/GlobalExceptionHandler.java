package com.spring.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.userservice.responsedto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(GenericException.class)
	public ResponseEntity<ErrorResponse> genericExceptionHandler(GenericException genericException) {
		final String errorMessage = genericException.getMessage();
		final String fieldName = genericException.getFieldName();
		final HttpStatus errorCode = genericException.getHttpStatusCode();
		ErrorResponse response = new ErrorResponse(fieldName, errorMessage, false);
		return new ResponseEntity<>(response, errorCode);
	}

}
