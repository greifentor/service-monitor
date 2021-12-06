package de.ollie.servicemonitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.converter.xml.WebClientResultToMapConverter;
import de.ollie.servicemonitor.evaluation.CheckExpressionEvaluator;
import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckRequest.ReturnedMediaType;
import de.ollie.servicemonitor.model.CheckResult;
import de.ollie.servicemonitor.model.CheckResult.Status;
import de.ollie.servicemonitor.model.Output;
import de.ollie.servicemonitor.web.WebClient;

@ExtendWith(MockitoExtension.class)
class CheckServiceTest {

	private static final String AUTHENTICATION_BEARER = "authentication bearer";
	private static final String CALL_RESULT = "call result";
	private static final String CHECK_EXPRESSION = "check expression";
	private static final String HOST = "host";
	private static final String NAME = "name";
	private static final Output OUTPUT = new Output();
	private static final WebClient.Response RESPONSE = new WebClient.Response(CALL_RESULT, WebClient.Status.OK);
	private static final ReturnedMediaType RETURNED_MEDIA_TYPE = ReturnedMediaType.JSON;
	private static final String URL = CheckRequest.HTTP_PROTOCOL + HOST;
	private static final Map<String, Object> VALUE_MAP = Map.of();

	@Mock
	private CheckExpressionEvaluator checkExpressionEvaluator;
	@Mock
	private WebClient webClient;
	@Mock
	private WebClientResultToMapConverter webClientResultToMapConverter;

	@InjectMocks
	private CheckService unitUnderTest;

	@Nested
	class TestsOfMethod_performCheck_CheckRequest {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValueAsCheckRequest_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.performCheck(null));
			}

		}

		@Nested
		class OkResult {

			@Test
			void passACheckRequestWhichResultsAnOKResult() {
				// Prepare
				CheckRequest checkRequest = new CheckRequest().setCheckExpression(CHECK_EXPRESSION)
						.setName(NAME)
						.setReturnedMediaType(RETURNED_MEDIA_TYPE)
						.setHost(HOST);
				CheckResult expected = new CheckResult().setCheckRequest(checkRequest)
						.setName(NAME)
						.setStatus(Status.OK)
						.setValueMap(VALUE_MAP);
				when(webClient.call(URL, null)).thenReturn(RESPONSE);
				when(webClientResultToMapConverter.convert(CALL_RESULT, RETURNED_MEDIA_TYPE)).thenReturn(VALUE_MAP);
				when(checkExpressionEvaluator.evaluate(CHECK_EXPRESSION, VALUE_MAP)).thenReturn(Boolean.TRUE);
				// Run
				CheckResult returned = unitUnderTest.performCheck(checkRequest);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void passACheckRequestWithAuthenticationBearerWhichResultsAnOKResult() {
				// Prepare
				CheckRequest checkRequest = new CheckRequest().setCheckExpression(CHECK_EXPRESSION)
						.setName(NAME)
						.setReturnedMediaType(RETURNED_MEDIA_TYPE)
						.setHost(HOST)
						.setAuthenticationBearer(AUTHENTICATION_BEARER);
				CheckResult expected = new CheckResult().setCheckRequest(checkRequest)
						.setName(NAME)
						.setStatus(Status.OK)
						.setValueMap(VALUE_MAP);
				when(webClient.call(URL, AUTHENTICATION_BEARER)).thenReturn(RESPONSE);
				when(webClientResultToMapConverter.convert(CALL_RESULT, RETURNED_MEDIA_TYPE)).thenReturn(VALUE_MAP);
				when(checkExpressionEvaluator.evaluate(CHECK_EXPRESSION, VALUE_MAP)).thenReturn(Boolean.TRUE);
				// Run
				CheckResult returned = unitUnderTest.performCheck(checkRequest);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class FailResult {

			@Test
			void passACheckRequestWhichResultsAnFAILResult() {
				// Prepare
				CheckRequest checkRequest = new CheckRequest().setCheckExpression(CHECK_EXPRESSION)
						.setReturnedMediaType(RETURNED_MEDIA_TYPE)
						.setHost(HOST);
				CheckResult expected = new CheckResult().setCheckRequest(checkRequest)
						.setStatus(Status.FAIL)
						.setValueMap(VALUE_MAP);
				when(webClient.call(URL, null)).thenReturn(RESPONSE);
				when(webClientResultToMapConverter.convert(CALL_RESULT, RETURNED_MEDIA_TYPE)).thenReturn(VALUE_MAP);
				when(checkExpressionEvaluator.evaluate(CHECK_EXPRESSION, VALUE_MAP)).thenReturn(Boolean.FALSE);
				// Run
				CheckResult returned = unitUnderTest.performCheck(checkRequest);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void webClientReturnResponseWithHTTPStatusNotOk() {
				// Prepare
				CheckRequest checkRequest = new CheckRequest().setCheckExpression(CHECK_EXPRESSION)
						.setReturnedMediaType(RETURNED_MEDIA_TYPE)
						.setHost(HOST);
				CheckResult expected = new CheckResult().setCheckRequest(checkRequest).setStatus(Status.FAIL);
				when(webClient.call(URL, null)).thenReturn(new WebClient.Response(null, WebClient.Status.BAD_REQUEST));
				// Run
				CheckResult returned = unitUnderTest.performCheck(checkRequest);
				// Check
				assertEquals(expected, returned);
				verifyNoInteractions(checkExpressionEvaluator);
				verifyNoInteractions(webClientResultToMapConverter);
			}

		}

		@Nested
		class ErrorResult {

			@Test
			void webClientThrowsAnException() {
				// Prepare
				String message = "message";
				CheckRequest checkRequest = new CheckRequest().setCheckExpression(CHECK_EXPRESSION)
						.setReturnedMediaType(RETURNED_MEDIA_TYPE)
						.setHost(HOST);
				CheckResult expected = new CheckResult().setCheckRequest(checkRequest)
						.setErrorMessage(message)
						.setStatus(Status.ERROR);
				when(webClient.call(URL, null)).thenThrow(new RuntimeException(message));
				// Run
				CheckResult returned = unitUnderTest
						.performCheck(checkRequest);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}