package de.ollie.servicemonitor.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.ollie.servicemonitor.evaluation.model.EqualsOperatorParser;
import de.ollie.servicemonitor.evaluation.model.EqualsOperatorParser.EqualsOperatorExpression;
import de.ollie.servicemonitor.evaluation.model.ExecutableExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorParser;
import de.ollie.servicemonitor.evaluation.model.ValueExpression;

class CheckExpressionParserTest {

	private List<OperatorParser> operatorParsers;

	private CheckExpressionParser unitUnderTest;

	@BeforeEach
	void setUp() {
		operatorParsers = new ArrayList<>();
		unitUnderTest = new CheckExpressionParser(operatorParsers);
	}

	@Nested
	class TestsOfMethod_parse_String {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValue_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.parse(null));
			}

		}

		@Nested
		class ReturnsCorrectsExecutableExpressionLists {

			@Test
			void passAnEmptyString_returnsAnEmptyList() {
				assertTrue(unitUnderTest.parse("").isEmpty());
			}

			@Nested
			class ValueExpressions {

				@Test
				void passAStringWithAnIntegerValue_returnsAListWithTheValue() {
					// Prepare
					Integer value = 42;
					List<ExecutableExpression> expected = List.of(new ValueExpression().setValue(value));
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse("" + value);
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void passAStringWithAnLongValue_returnsAListWithTheValue() {
					// Prepare
					Long value = Long.valueOf(Long.MAX_VALUE);
					List<ExecutableExpression> expected = List.of(new ValueExpression().setValue(value));
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse("" + value);
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void passAStringWithAnStringValue_returnsAListWithTheValue() {
					// Prepare
					String value = "string";
					List<ExecutableExpression> expected = List.of(new ValueExpression().setValue(value));
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse(value);
					// Check
					assertEquals(expected, returned);
				}

			}

			@Nested
			class OperatorExpressions {

				@Test
				void passAStringWithAnEqualsOperator_returnsAListWithTheOperator() {
					// Prepare
					String operatorStr = "EQUALS";
					List<ExecutableExpression> expected = List.of(new EqualsOperatorExpression());
					operatorParsers.add(new EqualsOperatorParser());
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse(operatorStr);
					// Check
					assertEquals(expected, returned);
				}

			}

		}

	}

}