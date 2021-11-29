package de.ollie.servicemonitor.converter.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.model.CheckRequest.ReturnedMediaType;

@ExtendWith(MockitoExtension.class)
class WebClientResultToMapConverterTest {

	private static final String VALUE_0 = "string";
	private static final Long VALUE_1 = 42L;

	private WebClientResultToMapConverter unitUnderTest;

	@BeforeEach
	void setUp() {
		unitUnderTest = new WebClientResultToMapConverter(new XMLStringToMapConverter());
	}

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

			@Test
			void passAnEmptyStringAndReturnedMediaTypeString_returnsAMapWithEmptyStringEntry() {
				// Prepare
				String value = "";
				Map<String, Object> expected = Map.of("STRING", "");
				// Run
				Map<String, Object> returned = unitUnderTest.convert(value, ReturnedMediaType.STRING);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class ReturnedMediaTypeJson {

			@Test
			void passAStringAndReturnedMediaTypeString_returnsAMapWithOnlyFieldSTRINGAndThePassedValue() {
				// Prepare
				String value = "{ \"field0\": \"" + VALUE_0 + "\", \"field1\": " + VALUE_1 + " }";
				Map<String, Object> expected = Map.of("field0", VALUE_0, "field1", VALUE_1);
				// Run
				Map<String, Object> returned = unitUnderTest.convert(value, ReturnedMediaType.JSON);
				// Check
				assertEquals(2, returned.size());
				assertEquals(VALUE_0, returned.get("field0"));
				assertEquals(VALUE_1.intValue(), returned.get("field1"));
			}

			@Test
			void passAnEmptyStringAndReturnedMediaTypeString_throwsAnException() {
				assertThrows(RuntimeException.class, () -> unitUnderTest.convert("", ReturnedMediaType.JSON));
			}

		}

		@Nested
		class ReturnedMediaTypeXml {

			@Test
			void passAStringAndReturnedMediaTypeString_returnsACorrectMapConvertingAllDataToString() {
				// Prepare
				String value = "<root><field0>" + VALUE_0 + "</field0><field1>" + VALUE_1 + "</field1></root>";
				// Run
				Map<String, Object> returned = unitUnderTest.convert(value, ReturnedMediaType.XML);
				// Check
				assertEquals(2, returned.size());
				assertEquals(VALUE_0, returned.get("field0"));
				assertEquals("" + VALUE_1, returned.get("field1"));
			}

			@Test
			void passAnEmptyStringAndReturnedMediaTypeString_returnsAnEmptyMap() {
				// Prepare
				String value = "";
				Map<String, Object> expected = Map.of();
				// Run
				Map<String, Object> returned = unitUnderTest.convert(value, ReturnedMediaType.XML);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}