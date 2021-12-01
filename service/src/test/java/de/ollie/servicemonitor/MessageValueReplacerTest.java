package de.ollie.servicemonitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.model.CheckRequest;

@ExtendWith(MockitoExtension.class)
class MessageValueReplacerTest {

	private static final String URL = "url";

	@InjectMocks
	private MessageValueReplacer unitUnderTest;

	@Nested
	class TestsOfMethod_getMessageWithReplacesValues_CheckRequest {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValueAscheckRequest_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getMessageWithReplacesValues(null));
			}

			@Test
			void checkRequestMessageIsNull() {
				// Prepare
				CheckRequest checkRequest = mock(CheckRequest.class);
				// Run & Check
				assertNull(unitUnderTest.getMessageWithReplacesValues(checkRequest));
				verify(checkRequest, times(1)).getMessage();
				verifyNoMoreInteractions(checkRequest);
			}

		}

		@Nested
		class Replacements {

			@Test
			void replacementForUrl() {
				// Prepare
				String message = "a message with ${url} content";
				String expected = message.replace("${url}", URL);
				CheckRequest checkRequest = mock(CheckRequest.class);
				when(checkRequest.getMessage()).thenReturn(message);
				when(checkRequest.getUrl()).thenReturn(URL);
				// Run
				String returned = unitUnderTest.getMessageWithReplacesValues(checkRequest);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}
