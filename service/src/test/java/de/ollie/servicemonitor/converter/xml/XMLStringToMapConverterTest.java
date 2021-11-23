package de.ollie.servicemonitor.converter.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXParseException;

@ExtendWith(MockitoExtension.class)
public class XMLStringToMapConverterTest {

	@InjectMocks
	private XMLStringToMapConverter unitUnderTest;

	@Nested
	class TestsOfMethod_convert_String {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValueAsXMLString_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.convert(null));
			}

			@Test
			void passAnInalidXMLString_throwsAnException() {
				assertThrows(SAXParseException.class, () -> unitUnderTest.convert(";op"));
			}

		}

		@Nested
		class ResultReturned {

			@Test
			void passASimpleValidXMLString_returnsACorrectMap() throws Exception {
				// Prepare
				String content = "content";
				String tagRoot = "root";
				String tagTag = "tag";
				String xmlString = "<" + tagRoot + "><" + tagTag + ">" + content + "</" + tagTag + "></" + tagRoot
						+ ">";
				Map<String, Object> expected = Map.of(tagTag, content);
				// Run
				Map<String, Object> returned = unitUnderTest.convert(xmlString);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void passAValidXMLString_returnsACorrectMap() throws Exception {
				// Prepare
				String xmlString = "<person><name>Name</name><date-of-birth><day>1</day><month>2</month><year>3</year></date-of-birth></person>";
				Map<String, Object> expected = Map
						.of("name", "Name", "date-of-birth", Map.of("day", "1", "month", "2", "year", "3"));
				// Run
				Map<String, Object> returned = unitUnderTest.convert(xmlString);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}