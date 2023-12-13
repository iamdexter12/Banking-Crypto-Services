package com.fintechharbor.notification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fintechharbor.notification.responsedto.ErrorResponse;

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

	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<ErrorResponse> webClientExceptionHandler(WebClientResponseException webClientException) {
		final String errorMessage = webClientException.getMessage();
		final HttpStatusCode errorCode = webClientException.getStatusCode();
		ErrorResponse response = new ErrorResponse("Webclient", errorMessage, false);
		return new ResponseEntity<>(response, errorCode);
	}

}
