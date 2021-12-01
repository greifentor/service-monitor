package de.ollie.servicemonitor.web;

import javax.inject.Named;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

/**
 * @author ollie (26.11.2021)
 */
@Named
@RequiredArgsConstructor
public class WebClient {

	public enum Status {
		BAD_REQUEST,
		NOT_FOUND,
		OK,
		OTHER;
	}

	@Value
	@ToString
	@Generated
	public static class Response {

		private String body;
		private Status status;

		public boolean isOk() {
			return status == Status.OK;
		}

	}

	private final RestTemplateFactory restTemplateFactory;

	/**
	 * Calls the passed URL and returns the result as a string.
	 * 
	 * @param url The URL to call.
	 * @return The result of the call in a string.
	 */
	public Response call(String url) {
		RestTemplate restTemplate = restTemplateFactory.create();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		return new Response(response.getBody(), getStatus(response));
	}

	private Status getStatus(ResponseEntity<String> responseEntity) {
		switch (responseEntity.getStatusCode()) {
		case BAD_REQUEST:
			return Status.BAD_REQUEST;
		case NOT_FOUND:
			return Status.NOT_FOUND;
		case OK:
			return Status.OK;
		default:
			return Status.OTHER;
		}
	}

}
