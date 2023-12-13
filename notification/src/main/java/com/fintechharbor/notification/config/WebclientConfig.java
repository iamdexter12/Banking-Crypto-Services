package com.fintechharbor.notification.config;

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


}
