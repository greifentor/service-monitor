package de.ollie.servicemonitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckResult;
import de.ollie.servicemonitor.model.CheckResult.Status;

@ExtendWith(MockitoExtension.class)
class MessageValueReplacerTest {

	private static final String NAME = "name";
	private static final Status STATUS = Status.OK;
	private static final String URL = "url";

	private Map<String, Object> valueMap = new HashMap<>();

	@Mock
	private CheckRequest checkRequest;
	@Mock
	private CheckResult checkResult;

	@Spy
	private ValueMapPathExtractor valueMapPathExtractor;

	@InjectMocks
	private MessageValueReplacer unitUnderTest;

	private void trainMocks() {
		when(checkResult.getStatus()).thenReturn(STATUS);
		when(checkResult.getValueMap()).thenReturn(valueMap);
		when(checkRequest.getName()).thenReturn(NAME);
		when(checkRequest.getUrl()).thenReturn(URL);
	}

	@Nested
	class TestsOfMethod_getMessageWithReplacesValues_CheckRequest {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValueAsCheckRequest_throwsAnException() {
				assertThrows(IllegalArgumentException.class,
						() -> unitUnderTest.getMessageWithReplacesValues(null, null, checkResult));
			}

			@Test
			void passANullValueAsCheckResult_throwsAnException() {
				assertThrows(IllegalArgumentException.class,
						() -> unitUnderTest.getMessageWithReplacesValues(null, checkRequest, null));
			}

			@Test
			void checkRequestMessageIsNull() {
				// Prepare
				CheckRequest checkRequest = mock(CheckRequest.class);
				// Run & Check
				assertNull(unitUnderTest.getMessageWithReplacesValues(null, checkRequest, checkResult));
				verifyNoMoreInteractions(checkRequest);
			}

		}

		@Nested
		class Replacements {

			@Test
			void replacementForName() {
				// Prepare
				String message = "a message with ${name} content";
				String expected = message.replace("${name}", NAME);
				trainMocks();
				// Run
				String returned = unitUnderTest
						.getMessageWithReplacesValues(message, checkRequest, checkResult);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void replacementForStatus() {
				// Prepare
				String message = "a message with ${status} content";
				String expected = message.replace("${status}", STATUS.name());
				trainMocks();
				// Run
				String returned = unitUnderTest
						.getMessageWithReplacesValues(message, checkRequest, checkResult);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void replacementForUrl() {
				// Prepare
				String message = "a message with ${url} content";
				String expected = message.replace("${url}", URL);
				trainMocks();
				// Run
				String returned = unitUnderTest
						.getMessageWithReplacesValues(message, checkRequest, checkResult);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void replacementForValueMapEntry() {
				// Prepare
				String message = "a message with $F{map.field} content";
				String expected = message.replace("$F{map.field}", URL);
				trainMocks();
				valueMap.put("map", Map.of("field", URL));
				// Run
				String returned = unitUnderTest
						.getMessageWithReplacesValues(message, checkRequest, checkResult);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}
