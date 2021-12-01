package de.ollie.servicemonitor.evaluation;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.ExecutableExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorExpression;
import de.ollie.servicemonitor.evaluation.model.ValueExpression;
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
		List<ExecutableExpression> executableExpressions = checkExpressionParser.parse(checkExpression);
		Stack<Object> runtimeStack = new Stack<Object>();
		return evaluateStack(executableExpressions, runtimeStack, valueMap);
	}

	private Object evaluateStack(List<ExecutableExpression> executableExpressions, Stack<Object> runtimeStack,
			Map<String, Object> valueMap) {
		executableExpressions.forEach(executableExpression -> {
			if (executableExpression instanceof ValueExpression) {
				runtimeStack.push(((ValueExpression) executableExpression).getValue());
			} else if (executableExpression instanceof OperatorExpression) {
				((OperatorExpression) executableExpression).exec(runtimeStack, valueMap);
			}
		});
		return runtimeStack.peek();
	}

}