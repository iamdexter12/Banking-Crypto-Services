package com.spring.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfig {

    /**
     * Creates a WebClient builder for making reactive HTTP requests.
     * 
     * @return WebClient.Builder instance
     */
    @Bean
    WebClient.Builder saltedgeWebClientBuilder() {
		return WebClient.builder().defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
	}

    /**
     * Creates a WebClient for making reactive HTTP requests.
     * 
     * @return WebClient instance
     */
    @Bean
    WebClient saltedgeWebClient() {
		return saltedgeWebClientBuilder().build();
	}

	/**
	 * Performs a GET request to the specified URL and retrieves the response as the
	 * given response type.
	 * 
	 * @param url          URL to send the GET request to
	 * @param responseType Class representing the type of the response
	 * @param <T>          Type of the response
	 * @return Response of the specified type
	 */
	public <T> T get(String url, Class<T> responseType) {
		return saltedgeWebClient().get().uri(url).retrieve().bodyToMono(responseType).block();
	}

	/**
	 * Performs a POST request to the specified URL with the given request body and
	 * retrieves the response as the given response type.
	 * 
	 * @param url          URL to send the POST request to
	 * @param requestBody  Request body to include in the POST request
	 * @param responseType Class representing the type of the response
	 * @param <T>          Type of the response
	 * @return Response of the specified type
	 */
	public <T> T post(String url, Object requestBody, Class<T> responseType) {
		return saltedgeWebClient().post().uri(url).body(BodyInserters.fromValue(requestBody)).retrieve()
				.bodyToMono(responseType).block();
	}

	
	/**
	 * Handles a WebClientResponseException by checking the HTTP status code.
	 *
	 * @param e The WebClientResponseException to handle.
	 * @throws NotFoundException if the HTTP status code is NOT_FOUND (404). It
	 *                           throws a SaltedgeConnectionFailedException for all
	 *                           other status codes.
	 */
//	public void handleWebClientResponseException(WebClientResponseException e) {
//		HttpStatusCode statusCode = e.getStatusCode();
//		if (statusCode == HttpStatus.NOT_FOUND) {
//			throw new NotFound("id", ExceptionConstant.CUSTOMER_OR_CONNECTION_NOT_FOUND);
//		} else if (e.getStatusText().equals("Not Acceptable")) {
//			throw new SessionRefreshRequestFailed();
//		} else {
//			throw new SaltedgeConnectionFailedException();
//		}
//	}

	/**
	 * Reads and parses a JSON response into an object of the specified valueType.
	 *
	 * @param jsonResponse The JSON response string to parse.
	 * @param valueType    The class representing the type of object to parse the
	 *                     JSON into.
	 * @param <T>          The type of the resulting object.
	 * @return An object of the specified type parsed from the JSON response.
	 * @throws JsonParsingException if there is an issue parsing the JSON response.
	 */
//	public <T> T readJsonResponse(String jsonResponse, Class<T> valueType) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			JsonNode jsonNode = objectMapper.readTree(jsonResponse);
//			JsonNode dataNode = jsonNode.get("data");
//			return objectMapper.treeToValue(dataNode, valueType);
//		} catch (JsonProcessingException e) {
//			throw new JsonParsingException("response", ExceptionConstant.PARSING_ERROR);
//		}
//	}

}


