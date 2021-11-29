package de.ollie.servicemonitor.converter.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.model.CheckRequest.ReturnedMediaType;

@ExtendWith(MockitoExtension.class)
class WebClientResultToMapConverterTest {

	@InjectMocks
	private WebClientResultToMapConverter unitUnderTest;

	@Nested
	class TestsOfMethod_convert_String_ReturnedMediaType {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValueAsString_returnsANullValue() {
				assertNull(unitUnderTest.convert(null, ReturnedMediaType.JSON));
			}

			@Test
			void passANullValueAsReturnedMediaType_returnsANullValue() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.convert(";op", null));
			}

		}

		@Nested
		class ReturnedMediaTypeString {

			@Test
			void passAStringAndReturnedMediaTypeString_returnsAMapWithOnlyFieldSTRINGAndThePassedValue() {
				// Prepare
				String value = "value";
				Map<String, Object> expected = Map.of("STRING", value);
				// Run
				Map<String, Object> returned = unitUnderTest.convert(value, ReturnedMediaType.STRING);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}