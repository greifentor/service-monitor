package de.ollie.servicemonitor.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.evaluation.parser.EqualsOperatorParser;
import de.ollie.servicemonitor.evaluation.parser.ReadValueOperatorParser;

@ExtendWith(MockitoExtension.class)
class CheckExpressionEvaluatorTest {

	private Map<String, Object> valueMap;

	@Spy
	private CheckExpressionParser checkExpressionParser = new CheckExpressionParser(
			List.of(new EqualsOperatorParser(), new ReadValueOperatorParser()));

	@InjectMocks
	private CheckExpressionEvaluator unitUnderTest;

	@BeforeEach
	void setUp() {
		valueMap = new HashMap<>();
	}

	@Nested
	class TestsOfMethod_evaluate_ExecutableExpression_Stack_MapStringObject {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passRuntimeStackAsNullValue_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.evaluate(null, new HashMap<>()));
			}

			@Test
			void passValueMapAsNullValue_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.evaluate(",op", null));
			}

		}

		@Nested
		class ValuesOnly {

			@Test
			void passASingleStringValueExpression_returnsTheStringValue() {
				// Prepare
				String value = "string";
				// Run
				Object result = unitUnderTest.evaluate(value, valueMap);
				// Check
				assertEquals(value, result);
			}

			@Test
			void passMoreThanOneStringValueButValuesOnlyExpression_returnsTheLastValueOfTheExpression() {
				// Prepare
				String value0 = "value0";
				String value1 = "value1";
				String value2 = "value2";
				// Run
				Object result = unitUnderTest.evaluate(value0 + " " + value1 + " " + value2, valueMap);
				// Check
				assertEquals(value2, result);
			}

		}

		@Nested
		class ExpressionsWithOperators {

			@Test
			void passAnExpressionWithTwoUnequalValuesAndAnEqualsOperator_returnsTrue() {
				// Prepare
				String value0 = "value0";
				String value1 = "value1";
				// Run
				Object result = unitUnderTest.evaluate(value0 + " " + value1 + " EQUALS", valueMap);
				// Check
				assertFalse((Boolean) result);
			}

			@Test
			void passAnExpressionWithTwoEqualValuesAndAnEqualsOperator_returnsFalse() {
				// Prepare
				String value0 = "value0";
				String value1 = "value0";
				// Run
				Object result = unitUnderTest.evaluate(value0 + " " + value1 + " EQUALS", valueMap);
				// Check
				assertTrue((Boolean) result);
			}

			@Test
			void passAnExpressionWithThreeEqualValuesAndThreeEqualsOperators_returnsTrue() {
				// Prepare
				String value0 = "value0";
				// Run
				Object result = unitUnderTest.evaluate(
						value0 + " " + value0 + " EQUALS " + value0 + " " + value0 + " EQUALS EQUALS",
						valueMap);
				// Check
				assertTrue((Boolean) result);
			}

			@Test
			void passAnExpressionWithTwoDifferentAndTwoEqualValuesAndThreeEqualsOperators_returnsFalse() {
				// Prepare
				String value0 = "value0";
				String value1 = "value1";
				// Run
				Object result = unitUnderTest.evaluate(
						value0 + " " + value0 + " EQUALS " + value1 + " " + value0 + " EQUALS EQUALS",
						valueMap);
				// Check
				assertFalse((Boolean) result);
			}

		}

	}

}
