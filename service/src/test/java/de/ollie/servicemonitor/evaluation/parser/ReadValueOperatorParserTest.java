package de.ollie.servicemonitor.evaluation.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.evaluation.model.ExecutableExpression.Type;
import de.ollie.servicemonitor.evaluation.parser.ReadValueOperatorParser.ReadValueOperatorExpression;

@ExtendWith(MockitoExtension.class)
class ReadValueOperatorParserTest {

	@InjectMocks
	private ReadValueOperatorParser unitUnderTest;

	@InjectMocks
	private ReadValueOperatorExpression expressionUnderTest;

	@Test
	void getOperatorToken_returnTheCorrectToken() {
		assertEquals("READ_VALUE", unitUnderTest.getOperatorToken());
	}

	@Test
	void getCreateOperatorExpression_returnsACorrectExpression() {
		assertTrue(unitUnderTest.createOperatorExpression() instanceof ReadValueOperatorExpression);
	}

	@Nested
	class TestOfClass_ReadValueOperatorExpression {

		@Nested
		class TestsOfMethod_exec_StackObject_MapStringObject {

			@Nested
			class ExceptionalBehavior {

				@Test
				void passAStackWithAnUnmatchingDataPath_throwsAnException() {
					// Prepare
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push("root.field");
					// Run & Check
					assertThrows(NoSuchElementException.class, () -> expressionUnderTest.exec(runtimeStack, Map.of()));
				}

			}

			@Nested
			class CleanRuns {

				@Test
				void getType_returnsTypeOPERATOR() {
					assertEquals(Type.OPERATOR, expressionUnderTest.getType());
				}

				@Test
				void passAStackWithAValidPathAndAMapWithMatchingData_pushesTheValueInTheMapToTheStack() {
					// Prepare
					String value = "value";
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push("root.field");
					Map<String, Object> valueMap = Map.of("root", Map.of("field", value));
					// Run
					expressionUnderTest.exec(runtimeStack, valueMap);
					// Check
					assertEquals(1, runtimeStack.size());
					assertEquals(value, runtimeStack.pop());
				}

			}

		}

	}

}
