package de.ollie.servicemonitor.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

@ExtendWith(MockitoExtension.class)
class ApplicationArgumentsToCallParametersConverterTest {

	@Mock
	private ApplicationArguments args;

	@InjectMocks
	private ApplicationArgumentsToCallParametersConverter unitUnderTest;

	@Nested
	class TestsOfMethod_convert_ApplicationArguments {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValue_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.convert(null));
			}

			@Test
			void passApplicationArgumentsMissingFileOption_throwsAnException() {
				// Prepare
				when(args.containsOption("file")).thenReturn(false);
				// Run & Check
				MissingOptionException e = assertThrows(MissingOptionException.class,
						() -> unitUnderTest.convert(args));
				assertEquals("missing option with name: file", e.getMessage());
				assertEquals("file", e.getOptionName());
			}

		}

		@Nested
		class RegularBehavior {

			@Test
			void passApplicationArgumentsWithFileOptionSet_returnsCallParametersWithCorrectlySetConfigurationFileName() {
				// Prepare
				String fileName = "file\na.me";
				when(args.containsOption("file")).thenReturn(true);
				when(args.getOptionValues("file")).thenReturn(List.of(fileName));
				// Run
				CallParameters returned = unitUnderTest.convert(args);
				// Check
				assertEquals(1, returned.getConfigurationFileNames().size());
				assertEquals(fileName, returned.getConfigurationFileNames().get(0));
			}

		}

	}

}
