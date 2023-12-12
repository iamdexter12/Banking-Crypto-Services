package com.spring.userservice.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseHandler {

	/**
	 * Generate a ResponseEntity with a custom message, HTTP status, and a response
	 * object.
	 *
	 * @param message     The custom message to include in the response.
	 * @param status      The HTTP status to set for the response.
	 * @param responseObj The response object to include in the response.
	 * @return A ResponseEntity containing the custom message, HTTP status, and
	 *         response object.
	 */
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("status", status);
		map.put("data", responseObj);
		return new ResponseEntity<>(map, status);
	}

	/**
	 * Generate a ResponseEntity with a custom message and HTTP status.
	 *
	 * @param message The custom message to include in the response.
	 * @param status  The HTTP status to set for the response.
	 * @return A ResponseEntity containing the custom message and HTTP status.
	 */
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("status", status);
		return new ResponseEntity<>(map, status);
	}

	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj,
			List<Link> links) {

		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("status", status);
		map.put("data", responseObj);
		map.put("link", links);
		return new ResponseEntity<>(map, status);
	}
}
