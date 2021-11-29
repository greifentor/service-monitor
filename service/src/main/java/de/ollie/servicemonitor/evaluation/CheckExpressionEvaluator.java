package de.ollie.servicemonitor.evaluation;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.Map;
import java.util.Stack;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.Operator;
import de.ollie.servicemonitor.evaluation.parser.EqualsOperatorParser.EqualsOperator;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor
public class CheckExpressionEvaluator {

	private final CheckExpressionParser checkExpressionParser;

	/**
	 * Evaluates the passed check expression. The passed map contains values usable during the process.
	 * 
	 * @param checkExpression The check expression to evaluate.
	 * @param valueMap        A map with a value storage which could be used while expression evaluation.
	 * @return The result of the evaluation.
	 */
	public Object evaluate(String checkExpression, Map<String, Object> valueMap) {
		ensure(checkExpression != null, "check expression stack cannot be null.");
		ensure(valueMap != null, "value map cannot be null.");
		Stack<Object> runtimeStack = checkExpressionParser.parse(checkExpression);
		return evaluateStack(runtimeStack, valueMap);
	}

	private Object evaluateStack(Stack<Object> runtimeStack, Map<String, Object> valueMap) {
		Object peek = runtimeStack.peek();
		if (!(peek instanceof Operator)) {
			return peek;
		} else if (peek instanceof EqualsOperator) {
			runtimeStack.pop();
			Object obj0 = runtimeStack.pop();
			Object obj1 = runtimeStack.pop();
			return obj0.equals(obj1);
		}
		return null;
	}

}