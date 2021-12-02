package de.ollie.servicemonitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValueMapPathExtractorTest {

	private static final String PATH = "a.path";

	@InjectMocks
	private ValueMapPathExtractor unitUnderTest;

	@Nested
	class TestsOfMethod_extractPath_String {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValueAsString_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.extractPath(null));
			}

			@Test
			void passStringWithAnUnclosedPath_returnsAnOptionalWithThePath() {
				assertThrows(StringIndexOutOfBoundsException.class,
						() -> unitUnderTest.extractPath("$F{" + PATH).get());
			}

		}

		@Nested
		class CleanRuns {

			@Test
			void passStringWithNoPath_returnsAEmptyOptional() {
				assertTrue(unitUnderTest.extractPath(";op").isEmpty());
			}

			@Test
			void passStringWithAPath_returnsAnOptionalWithThePath() {
				assertEquals(PATH, unitUnderTest.extractPath("$F{" + PATH + "}").get());
			}

		}

	}

}
