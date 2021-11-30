package de.ollie.servicemonitor.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.ollie.servicemonitor.evaluation.model.ExecutableExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorExpressionParser;
import de.ollie.servicemonitor.evaluation.model.ValueExpression;
import de.ollie.servicemonitor.evaluation.parser.EqualsOperatorParser;
import de.ollie.servicemonitor.evaluation.parser.EqualsOperatorParser.EqualsOperatorExpression;
import de.ollie.servicemonitor.evaluation.parser.ReadValueOperatorParser;
import de.ollie.servicemonitor.evaluation.parser.ReadValueOperatorParser.ReadValueOperatorExpression;

class CheckExpressionParserTest {

	private List<OperatorExpressionParser> operatorParsers;

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
			void passAnEmptyString_returnsAnEmptyStack() {
				assertTrue(unitUnderTest.parse("").isEmpty());
			}

			@Nested
			class ValueExpressions {

				@Test
				void passAStringWithAnIntegerValue_returnsAStackWithTheValue() {
					// Prepare
					Integer value = 42;
					List<ExecutableExpression> expected = new ArrayList<>();
					expected.add(new ValueExpression().setValue(Long.valueOf(value)));
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse("" + value);
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void passAStringWithAnLongValue_returnsAStackWithTheValue() {
					// Prepare
					Long value = Long.valueOf(Long.MAX_VALUE);
					List<ExecutableExpression> expected = new ArrayList<>();
					expected.add(new ValueExpression().setValue(Long.valueOf(value)));
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse("" + value);
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void passAStringWithAnStringValue_returnsAStackWithTheValue() {
					// Prepare
					String value = "string";
					List<ExecutableExpression> expected = new ArrayList<>();
					expected.add(new ValueExpression().setValue(value));
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse(value);
					// Check
					assertEquals(expected, returned);
				}

			}

			@Nested
			class OperatorExpressions {

				@Test
				void passAStringWithAnEqualsOperator_returnsAStackWithTheOperator() {
					// Prepare
					OperatorExpressionParser operatorParser = new EqualsOperatorParser();
					String operatorStr = operatorParser.getOperatorToken();
					List<ExecutableExpression> expected = new ArrayList<>();
					expected.add(new EqualsOperatorExpression());
					operatorParsers.add(operatorParser);
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse(operatorStr);
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void passAStringWithAnReadValueOperator_returnsAStackWithTheOperator() {
					// Prepare
					OperatorExpressionParser operatorParser = new ReadValueOperatorParser();
					String operatorStr = operatorParser.getOperatorToken();
					List<ExecutableExpression> expected = new ArrayList<>();
					expected.add(new ReadValueOperatorExpression());
					operatorParsers.add(operatorParser);
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse(operatorStr);
					// Check
					assertEquals(expected, returned);
				}

			}

			@Nested
			class MixedExpressions {

				@Test
				void passAStringWithValuesAndAnOperator_returnsACorrectStack() {
					// Prepare
					String value0 = "string";
					Long value1 = 1L;
					OperatorExpressionParser operatorParser = new EqualsOperatorParser();
					String expressionStr = value0 + " " + value1 + " " + operatorParser.getOperatorToken();
					List<ExecutableExpression> expected = new ArrayList<>();
					expected.add(new ValueExpression().setValue(value0));
					expected.add(new ValueExpression().setValue(value1));
					expected.add(new EqualsOperatorExpression());
					operatorParsers.add(operatorParser);
					// Run
					List<ExecutableExpression> returned = unitUnderTest.parse(expressionStr);
					// Check
					assertEquals(expected, returned);
				}
				
			}
			
		}

	}

}