package de.ollie.servicemonitor.swing.parameter;

import static de.ollie.servicemonitor.swing.parameter.PropertiesToCallParameterConverter.FILE_OPTION_NAME;
import static de.ollie.servicemonitor.swing.parameter.PropertiesToCallParameterConverter.REPEAT_IN_SECONDS_OPTION_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.parameter.CallParameters;
import de.ollie.servicemonitor.parameter.IllegalValueException;
import de.ollie.servicemonitor.parameter.MissingOptionException;
import de.ollie.servicemonitor.parameter.ParameterTypeException;

@ExtendWith(MockitoExtension.class)
public class PropertiesToCallParameterConverterTest {

	private static final String FILE_NAME = "file\na.me";
	
	@Mock
	private Properties properties;

	@InjectMocks
	private PropertiesToCallParameterConverter unitUnderTest;
	
	private void trainMocksForFileParameter() {
		when(properties.containsKey(FILE_OPTION_NAME)).thenReturn(true);
		when(properties.getProperty(FILE_OPTION_NAME)).thenReturn(FILE_NAME);
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
				when(properties.containsKey(FILE_OPTION_NAME)).thenReturn(false);
				// Run & Check
				MissingOptionException e =
				        assertThrows(MissingOptionException.class, () -> unitUnderTest.convert(properties));
				assertEquals("missing option with name: " + FILE_OPTION_NAME, e.getMessage());
				assertEquals(FILE_OPTION_NAME, e.getOptionName());
			}

			@Test
			void passApplicationArgumentsWithRepeatInSecondsOptionSetNonLong_throwsAnException() {
				// Prepare
				String value = "a string";
				when(properties.containsKey(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(true);
				when(properties.getProperty(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(value);
				trainMocksForFileParameter();
				// Run & Check
				ParameterTypeException exception =
				        assertThrows(ParameterTypeException.class, () -> unitUnderTest.convert(properties));
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
				when(properties.containsKey(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(true);
				when(properties.getProperty(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn("" + value);
				trainMocksForFileParameter();
				// Run & Check
				IllegalValueException exception =
				        assertThrows(IllegalValueException.class, () -> unitUnderTest.convert(properties));
				assertEquals("0..Long.MAX_VALUE", exception.getExpectedValueBounds());
				assertEquals(
				        "value '" + value
				                + "' for parameter '"
				                + REPEAT_IN_SECONDS_OPTION_NAME
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
				CallParameters returned = unitUnderTest.convert(properties);
				// Check
				assertEquals(1, returned.getConfigurationFileNames().size());
				assertEquals(FILE_NAME, returned.getConfigurationFileNames().get(0));
			}

			@Test
			void passApplicationArgumentsWithFileOptionSetForTwoFileNames_returnsCallParametersWithCorrectlySetConfigurationFileName() {
				// Prepare
				when(properties.containsKey(FILE_OPTION_NAME)).thenReturn(true);
				when(properties.getProperty(FILE_OPTION_NAME)).thenReturn(FILE_NAME + 1 + "," + FILE_NAME + 2);
				// Run
				CallParameters returned = unitUnderTest.convert(properties);
				// Check
				assertEquals(2, returned.getConfigurationFileNames().size());
				assertEquals(FILE_NAME + 1, returned.getConfigurationFileNames().get(0));
				assertEquals(FILE_NAME + 2, returned.getConfigurationFileNames().get(1));
			}

			@Test
			void passApplicationArgumentsWithRepeatInSecondsOptionSet_returnsCallParametersWithCorrectlySetConfiguration() {
				// Prepare
				Long repeatInSeconds = 42L;
				when(properties.containsKey(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(true);
				when(properties.getProperty(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn("" + repeatInSeconds);
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(properties);
				// Check
				assertEquals(repeatInSeconds, returned.getRepeatInSeconds());
			}

			@Test
			void passApplicationArgumentsWithRepeatInSecondsNotOptionSet_returnsCallParametersWithANullAsRepeatInSeconds() {
				// Prepare
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(properties);
				// Check
				assertNull(returned.getRepeatInSeconds());
			}

			@Test
			void passApplicationArgumentsWithRepeatInSecondsOptionSetMoreThanOneTimes_returnsCallParametersWithFirstValue() {
				// Prepare
				Long repeatInSeconds = 42L;
				when(properties.containsKey(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn(true);
				when(properties.getProperty(REPEAT_IN_SECONDS_OPTION_NAME)).thenReturn("" + repeatInSeconds);
				trainMocksForFileParameter();
				// Run
				CallParameters returned = unitUnderTest.convert(properties);
				// Check
				assertEquals(repeatInSeconds, returned.getRepeatInSeconds());
			}

		}

	}

}