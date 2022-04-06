package de.ollie.servicemonitor.parameter;

import static de.ollie.servicemonitor.parameter.ApplicationArgumentsToCallParametersConverter.FILE_OPTION_NAME;
import static de.ollie.servicemonitor.parameter.ApplicationArgumentsToCallParametersConverter.FONT_SIZE_OPTION_NAME;
import static de.ollie.servicemonitor.parameter.ApplicationArgumentsToCallParametersConverter.REPEAT_IN_SECONDS_OPTION_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

	private static final String FILE_NAME = "file\na.me";
	private static final Integer FONT_SIZE = 6;
	private static final Long REPEAT_IN_SECONDS = 42L;

	@Mock
	private ApplicationArguments args;

	@InjectMocks
	private ApplicationArgumentsToCallParametersConverter unitUnderTest;

	private void trainMocksForFileParameter() {
		when(args.containsOption(FILE_OPTION_NAME)).thenReturn(true);
		when(args.getOptionValues(FILE_OPTION_NAME)).thenReturn(List.of(FILE_NAME));
	}

	private void trainMocksForFontSizeForCleanRun() {
		when(args.containsOption(FONT_SIZE_OPTION_NAME)).thenReturn(true);
		when(args.getOptionValues(FONT_SIZE_OPTION_NAME)).thenReturn(List.of("" + FONT_SIZE));
	}

	private void trainMocksForRepeatInSecondsForCleanRun() {
		when(args.containsOption(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(true);
		when(args.getOptionValues(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(List.of("" + REPEAT_IN_SECONDS));
	}

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
				when(args.containsOption(FILE_OPTION_NAME)).thenReturn(false);
				// Run & Check
				MissingOptionException e = assertThrows(MissingOptionException.class,
						() -> unitUnderTest.convert(args));
				assertEquals("missing option with name: " + FILE_OPTION_NAME, e.getMessage());
				assertEquals(FILE_OPTION_NAME, e.getOptionName());
			}

			@Test
			void passApplicationArgumentsWithFontSizeOptionSetNonLong_throwsAnException() {
				// Prepare
				String value = "a string";
				when(args.containsOption(FONT_SIZE_OPTION_NAME)).thenReturn(true);
				when(args.getOptionValues(FONT_SIZE_OPTION_NAME)).thenReturn(List.of(value));
				trainMocksForFileParameter();
				// Run & Check
				ParameterTypeException exception =
				        assertThrows(ParameterTypeException.class, () -> unitUnderTest.convert(args));
				assertEquals("Integer", exception.getExpectedTypeName());
				assertEquals(
				        "wrong type of parameter '" + FONT_SIZE_OPTION_NAME
				                + "'. Expected a 'Integer', but value does not match: '"
				                + value
				                + "'",
				        exception.getMessage());
				assertEquals(FONT_SIZE_OPTION_NAME, exception.getParameterName());
				assertEquals(value, exception.getValueContent());
			}

			@Test
			void passApplicationArgumentsWithNegativeFontSizeOptionSet_throwsAnException() {
				// Prepare
				Long value = -1L;
				when(args.containsOption(FONT_SIZE_OPTION_NAME)).thenReturn(true);
				when(args.getOptionValues(FONT_SIZE_OPTION_NAME)).thenReturn(List.of("" + value));
				trainMocksForFileParameter();
				// Run & Check
				IllegalValueException exception =
				        assertThrows(IllegalValueException.class, () -> unitUnderTest.convert(args));
				assertEquals("1..7", exception.getExpectedValueBounds());
				assertEquals(
				        "value '" + value + "' for parameter '" + FONT_SIZE_OPTION_NAME + "' is out of range: 1..7",
				        exception.getMessage());
				assertEquals(FONT_SIZE_OPTION_NAME, exception.getParameterName());
				assertEquals(value.intValue(), exception.getValueContent());
			}

			@Test
			void passApplicationArgumentsWithRepeatInSecondsOptionSetNonLong_throwsAnException() {
				// Prepare
				String value = "a string";
				when(args.containsOption(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(true);
				when(args.getOptionValues(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(List.of(value));
				trainMocksForFontSizeForCleanRun();
				trainMocksForFileParameter();
				// Run & Check
				ParameterTypeException exception = assertThrows(ParameterTypeException.class,
						() -> unitUnderTest.convert(args));
				assertEquals("Long", exception.getExpectedTypeName());
				assertEquals(
						"wrong type of parameter '" + REPEAT_IN_SECONDS_OPTION_NAME
				                + "'. Expected a 'Long', but value does not match: '"
				                + value
				                + "'",
						exception.getMessage());
				assertEquals(REPEAT_IN_SECONDS_OPTION_NAME, exception.getParameterName());
				assertEquals(value, exception.getValueContent());
			}

			@Test
			void passApplicationArgumentsWithNegativeRepeatInSecondsOptionSet_throwsAnException() {
				// Prepare
				Long value = -1L;
				when(args.containsOption(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(true);
				when(args.getOptionValues(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(List.of("" + value));
				trainMocksForFontSizeForCleanRun();
				trainMocksForFileParameter();
				// Run & Check
				IllegalValueException exception = assertThrows(IllegalValueException.class,
						() -> unitUnderTest.convert(args));
				assertEquals("0..Long.MAX_VALUE", exception.getExpectedValueBounds());
				assertEquals(
						"value '" + value + "' for parameter '" + REPEAT_IN_SECONDS_OPTION_NAME
				                + "' is out of range: 0..Long.MAX_VALUE",
						exception.getMessage());
				assertEquals(REPEAT_IN_SECONDS_OPTION_NAME, exception.getParameterName());
				assertEquals(value, exception.getValueContent());
			}

		}

		@Nested
		class RegularBehavior {

			@Test
			void passApplicationArgumentsWithFileOptionSet_returnsCallParametersWithCorrectlySetConfigurationFileName() {
				// Prepare
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(args);
				// Check
				assertEquals(1, returned.getConfigurationFileNames().size());
				assertEquals(FILE_NAME, returned.getConfigurationFileNames().get(0));
			}

			@Test
			void passApplicationArgumentsWithFontSizeOptionSet_returnsCallParametersWithCorrectlySetConfiguration() {
				// Prepare
				Integer fontSize = 4;
				when(args.containsOption(FONT_SIZE_OPTION_NAME)).thenReturn(true);
				when(args.getOptionValues(FONT_SIZE_OPTION_NAME)).thenReturn(List.of("" + fontSize));
				trainMocksForRepeatInSecondsForCleanRun();
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(args);
				// Check
				assertEquals(fontSize, returned.getFontSize());
			}

			@Test
			void passApplicationArgumentsWithFontSizeNotOptionSet_returnsCallParametersWith4ForFontSize() {
				// Prepare
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(args);
				// Check
				assertEquals(4, returned.getFontSize());
			}

			@Test
			void passApplicationArgumentsWithFontSizeOptionSetMoreThanOneTimes_returnsCallParametersWithFirstValue() {
				// Prepare
				Integer fontSize = 4;
				when(args.containsOption(FONT_SIZE_OPTION_NAME)).thenReturn(true);
				when(args.getOptionValues(FONT_SIZE_OPTION_NAME))
				        .thenReturn(List.of("" + fontSize, "" + (fontSize + 1)));
				trainMocksForRepeatInSecondsForCleanRun();
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(args);
				// Check
				assertEquals(fontSize, returned.getFontSize());
			}

			@Test
			void passApplicationArgumentsWithRepeatInSecondsOptionSet_returnsCallParametersWithCorrectlySetConfiguration() {
				// Prepare
				trainMocksForFileParameter();
				trainMocksForFontSizeForCleanRun();
				trainMocksForRepeatInSecondsForCleanRun();
				// Run
				CallParameters returned = unitUnderTest.convert(args);
				// Check
				assertEquals(REPEAT_IN_SECONDS, returned.getRepeatInSeconds());
			}

			@Test
			void passApplicationArgumentsWithRepeatInSecondsNotOptionSet_returnsCallParametersWithANullAsRepeatInSeconds() {
				// Prepare
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(args);
				// Check
				assertNull(returned.getRepeatInSeconds());
			}

			@Test
			void passApplicationArgumentsWithRepeatInSecondsOptionSetMoreThanOneTimes_returnsCallParametersWithFirstValue() {
				// Prepare
				when(args.containsOption(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(true);
				when(args.getOptionValues(REPEAT_IN_SECONDS_OPTION_NAME))
				        .thenReturn(List.of("" + REPEAT_IN_SECONDS, "" + (REPEAT_IN_SECONDS + 1)));
				trainMocksForFontSizeForCleanRun();
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(args);
				// Check
				assertEquals(REPEAT_IN_SECONDS, returned.getRepeatInSeconds());
			}

		}

	}

}
