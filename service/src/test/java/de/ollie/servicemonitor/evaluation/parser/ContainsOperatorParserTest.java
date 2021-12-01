package de.ollie.servicemonitor.evaluation.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.evaluation.model.ExecutableExpression.Type;
import de.ollie.servicemonitor.evaluation.parser.ContainsOperatorParser.ContainsOperatorExpression;

@ExtendWith(MockitoExtension.class)
class ContainsOperatorParserTest {

	@InjectMocks
	private ContainsOperatorParser unitUnderTest;

	@InjectMocks
	private ContainsOperatorExpression expressionUnderTest;

	@Test
	void getOperatorToken_returnTheCorrectToken() {
		assertEquals("CONTAINS", unitUnderTest.getOperatorToken());
	}

	@Test
	void getCreateOperatorExpression_returnsACorrectExpression() {
		assertTrue(unitUnderTest.createOperatorExpression() instanceof ContainsOperatorExpression);
	}

	@Nested
	class TestsOfClass_ContainsOperatorExpression {

		@Nested
		class TestsOfMethod_exec_StackObject_MapStringObject {

			@Nested
			class ExceptionalBehavior {

				@Test
				void passANullValueAsRuntimeStack_throwsAnException() {
					assertThrows(NullPointerException.class, () -> expressionUnderTest.exec(null, Map.of()));
				}

				@Test
				void passARuntimeStackWithLessThanTwoElements_throwsAnException() {
					// Prepare
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push("value");
					// Run & Check
					assertThrows(EmptyStackException.class, () -> expressionUnderTest.exec(runtimeStack, Map.of()));
				}

			}

			@Nested
			class CleanRuns {

				@Test
				void getType_returnsTypeOPERATOR() {
					assertEquals(Type.OPERATOR, expressionUnderTest.getType());
				}

				@Test
				void passTwoElements_SecondIsContainedInFirstOne_stackWithBooleanTRUERemains() {
					// Prepare
					String value0 = "value";
					String value1 = "val";
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value0);
					runtimeStack.push(value1);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.TRUE, runtimeStack.peek());
				}

				@Test
				void passTwoElements_SecondEqualsTheFirstOne_stackWithBooleanTRUERemains() {
					// Prepare
					String value = "value";
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value);
					runtimeStack.push(value);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.TRUE, runtimeStack.peek());
				}

				@Test
				void passTwoElements_SecondIsNotContainedInFirstOne_stackWithBooleanFALSERemains() {
					// Prepare
					String value0 = "value";
					String value1 = "not contained";
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value0);
					runtimeStack.push(value1);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.FALSE, runtimeStack.peek());
				}

				@Test
				void passTwoNumberElements_SecondIsContainedInFirstOne_stackWithBooleanTRUERemains() {
					// Prepare
					Long value0 = 1701L;
					Long value1 = 70L;
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value0);
					runtimeStack.push(value1);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.TRUE, runtimeStack.peek());
				}

				@Test
				void passTwoNumberElements_SecondEqualsTheFirstOne_stackWithBooleanTRUERemains() {
					// Prepare
					Long value = 42L;
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value);
					runtimeStack.push(value);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.TRUE, runtimeStack.peek());
				}

				@Test
				void passTwoNumberElements_SecondIsNotContainedInFirstOne_stackWithBooleanFALSERemains() {
					// Prepare
					Long value0 = 1701L;
					Long value1 = 42L;
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value0);
					runtimeStack.push(value1);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.FALSE, runtimeStack.peek());
				}

				@Test
				void passTwoMixedElements_SecondIsContainedInFirstOne_stackWithBooleanTRUERemains() {
					// Prepare
					Long value0 = 1701L;
					String value1 = "70";
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value0);
					runtimeStack.push(value1);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.TRUE, runtimeStack.peek());
				}

				@Test
				void passTwoMixedElements_SecondEqualsTheFirstOne_stackWithBooleanTRUERemains() {
					// Prepare
					Long value0 = 42L;
					String value1 = "42";
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value0);
					runtimeStack.push(value1);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.TRUE, runtimeStack.peek());
				}

				@Test
				void passTwoMixedElements_SecondIsNotContainedInFirstOne_stackWithFALSEtringRemains() {
					// Prepare
					Long value0 = 1701L;
					String value1 = "42";
					Stack<Object> runtimeStack = new Stack<>();
					runtimeStack.push(value0);
					runtimeStack.push(value1);
					// Run
					expressionUnderTest.exec(runtimeStack, Map.of());
					// Check
					assertEquals(Boolean.FALSE, runtimeStack.peek());
				}

			}

		}

	}

}