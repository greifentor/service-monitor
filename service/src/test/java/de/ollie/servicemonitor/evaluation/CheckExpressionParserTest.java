package de.ollie.servicemonitor.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.ollie.servicemonitor.evaluation.model.OperatorParser;
import de.ollie.servicemonitor.evaluation.parser.EqualsOperatorParser;
import de.ollie.servicemonitor.evaluation.parser.EqualsOperatorParser.EqualsOperator;
import de.ollie.servicemonitor.evaluation.parser.ReadValueOperatorParser;
import de.ollie.servicemonitor.evaluation.parser.ReadValueOperatorParser.ReadValueOperator;

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
			void passAnEmptyString_returnsAnEmptyStack() {
				assertTrue(unitUnderTest.parse("").isEmpty());
			}

			@Nested
			class ValueExpressions {

				@Test
				void passAStringWithAnIntegerValue_returnsAStackWithTheValue() {
					// Prepare
					Integer value = 42;
					Stack<Object> expected = new Stack<>();
					expected.push(Long.valueOf(value));
					// Run
					Stack<Object> returned = unitUnderTest.parse("" + value);
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void passAStringWithAnLongValue_returnsAStackWithTheValue() {
					// Prepare
					Long value = Long.valueOf(Long.MAX_VALUE);
					Stack<Object> expected = new Stack<>();
					expected.push(value);
					// Run
					Stack<Object> returned = unitUnderTest.parse("" + value);
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void passAStringWithAnStringValue_returnsAStackWithTheValue() {
					// Prepare
					String value = "string";
					Stack<Object> expected = new Stack<>();
					expected.push(value);
					// Run
					Stack<Object> returned = unitUnderTest.parse(value);
					// Check
					assertEquals(expected, returned);
				}

			}

			@Nested
			class OperatorExpressions {

				@Test
				void passAStringWithAnEqualsOperator_returnsAStackWithTheOperator() {
					// Prepare
					OperatorParser operatorParser = new EqualsOperatorParser();
					String operatorStr = operatorParser.getOperatorToken();
					Stack<Object> expected = new Stack<>();
					expected.push(new EqualsOperator());
					operatorParsers.add(operatorParser);
					// Run
					Stack<Object> returned = unitUnderTest.parse(operatorStr);
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void passAStringWithAnReadValueOperator_returnsAStackWithTheOperator() {
					// Prepare
					OperatorParser operatorParser = new ReadValueOperatorParser();
					String operatorStr = operatorParser.getOperatorToken();
					Stack<Object> expected = new Stack<>();
					expected.push(new ReadValueOperator());
					operatorParsers.add(operatorParser);
					// Run
					Stack<Object> returned = unitUnderTest.parse(operatorStr);
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
					OperatorParser operatorParser = new EqualsOperatorParser();
					String expressionStr = value0 + " " + value1 + " " + operatorParser.getOperatorToken();
					Stack<Object> expected = new Stack<>();
					expected.push(value0);
					expected.push(value1);
					expected.push(new EqualsOperator());
					operatorParsers.add(operatorParser);
					// Run
					Stack<Object> returned = unitUnderTest.parse(expressionStr);
					// Check
					assertEquals(expected, returned);
				}
				
			}
			
		}

	}

}