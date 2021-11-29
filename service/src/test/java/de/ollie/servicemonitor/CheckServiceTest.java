package de.ollie.servicemonitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import de.ollie.servicemonitor.web.WebClient;

@ExtendWith(MockitoExtension.class)
class CheckServiceTest {

	private static final String CALL_RESULT = "call result";
	private static final String CHECK_EXPRESSION = "check expression";
	private static final ReturnedMediaType RETURNED_MEDIA_TYPE = ReturnedMediaType.JSON;
	private static final String URL = "url";
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
				CheckResult expected = new CheckResult().setStatus(Status.OK);
				when(webClient.call(URL)).thenReturn(CALL_RESULT);
				when(webClientResultToMapConverter.convert(CALL_RESULT, RETURNED_MEDIA_TYPE)).thenReturn(VALUE_MAP);
				when(checkExpressionEvaluator.evaluate(CHECK_EXPRESSION, VALUE_MAP)).thenReturn(Boolean.TRUE);
				// Run
				CheckResult returned =
						unitUnderTest
								.performCheck(
										new CheckRequest()
												.setCheckExpression(CHECK_EXPRESSION)
												.setReturnedMediaType(RETURNED_MEDIA_TYPE)
												.setUrl(URL));
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class FailResult {

			@Test
			void passACheckRequestWhichResultsAnFAILResult() {
				// Prepare
				CheckResult expected = new CheckResult().setStatus(Status.FAIL);
				when(webClient.call(URL)).thenReturn(CALL_RESULT);
				when(webClientResultToMapConverter.convert(CALL_RESULT, RETURNED_MEDIA_TYPE)).thenReturn(VALUE_MAP);
				when(checkExpressionEvaluator.evaluate(CHECK_EXPRESSION, VALUE_MAP)).thenReturn(Boolean.FALSE);
				// Run
				CheckResult returned =
						unitUnderTest
								.performCheck(
										new CheckRequest()
												.setCheckExpression(CHECK_EXPRESSION)
												.setReturnedMediaType(RETURNED_MEDIA_TYPE)
												.setUrl(URL));
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class ErrorResult {

			@Test
			void webClientThrowsAnException() {
				// Prepare
				CheckResult expected = new CheckResult().setStatus(Status.ERROR);
				when(webClient.call(URL)).thenThrow(new RuntimeException());
				// Run
				CheckResult returned =
						unitUnderTest
								.performCheck(
										new CheckRequest()
												.setCheckExpression(CHECK_EXPRESSION)
												.setReturnedMediaType(RETURNED_MEDIA_TYPE)
												.setUrl(URL));
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}