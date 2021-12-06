package de.ollie.servicemonitor.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.ollie.servicemonitor.web.WebClient.Response;
import de.ollie.servicemonitor.web.WebClient.Status;

@ExtendWith(MockitoExtension.class)
public class WebClientTest {

	private static final String AUTHENTICATION_BEARER = "authentication header";
	private static final String RETURNED_STRING = "returned string";
	private static final String URL = "url";

	@Mock
	private RestTemplate restTemplate;
	@Mock
	private RestTemplateFactory restTemplateFactory;

	@InjectMocks
	private WebClient unitUnderTest;

	@Nested
	class TestsOfMethod_call_String {

		@Test
		void webClientThrowsAnException_throwsAnException() {
			// Prepare
			RuntimeException exception = new RuntimeException();
			when(restTemplate.exchange(URL, HttpMethod.GET, null, String.class)).thenThrow(exception);
			when(restTemplateFactory.create()).thenReturn(restTemplate);
			// Run
			Throwable thrown = assertThrows(RuntimeException.class, () -> unitUnderTest.call(URL, null));
			// Check
			assertSame(exception, thrown);
		}

		@Test
		void webClientReturnsAString_returnsResponseWithStatusOkAndAString() {
			// Prepare
			Response expected = new Response(RETURNED_STRING, Status.OK);
			when(restTemplate.exchange(URL, HttpMethod.GET, null, String.class))
					.thenReturn(ResponseEntity.ok(RETURNED_STRING));
			when(restTemplateFactory.create()).thenReturn(restTemplate);
			// Run
			Response returned = unitUnderTest.call(URL, null);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void webClientReturnStatusBadRequest_returnsResponseWithStatusBadRequestAndANullString() {
			// Prepare
			Response expected = new Response(null, Status.BAD_REQUEST);
			when(restTemplate.exchange(URL, HttpMethod.GET, null, String.class))
					.thenReturn(ResponseEntity.badRequest().build());
			when(restTemplateFactory.create()).thenReturn(restTemplate);
			// Run
			Response returned = unitUnderTest.call(URL, null);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void webClientReturnStatusNotFound_returnsResponseWithStatusNotFoundAndANullString() {
			// Prepare
			Response expected = new Response(null, Status.NOT_FOUND);
			when(restTemplate.exchange(URL, HttpMethod.GET, null, String.class))
					.thenReturn(ResponseEntity.notFound().build());
			when(restTemplateFactory.create()).thenReturn(restTemplate);
			// Run
			Response returned = unitUnderTest.call(URL, null);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void webClientReturnStatusOther_returnsResponseWithStatusInternalServerErrorAndANullString() {
			// Prepare
			Response expected = new Response(null, Status.OTHER);
			when(restTemplate.exchange(URL, HttpMethod.GET, null, String.class))
					.thenReturn(ResponseEntity.internalServerError().build());
			when(restTemplateFactory.create()).thenReturn(restTemplate);
			// Run
			Response returned = unitUnderTest.call(URL, null);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void webClientCallWithAuthenticationBearer_callsTheRestTemplateWithACorrectHeader() {
			// Prepare
			Response expected = new Response(RETURNED_STRING, Status.OK);
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(AUTHENTICATION_BEARER);
			HttpEntity<String> httpEntity = new HttpEntity<>(headers);
			when(restTemplate.exchange(URL, HttpMethod.GET, httpEntity, String.class))
					.thenReturn(ResponseEntity.ok(RETURNED_STRING));
			when(restTemplateFactory.create()).thenReturn(restTemplate);
			// Run
			Response returned = unitUnderTest.call(URL, AUTHENTICATION_BEARER);
			// Check
			assertEquals(expected, returned);
		}

	}

}
