package de.ollie.servicemonitor.evaluation.parser;

import java.util.Map;
import java.util.Stack;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.OperatorExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorExpressionParser;
import lombok.EqualsAndHashCode;

@Named
public class EqualsOperatorParser implements OperatorExpressionParser {

	@EqualsAndHashCode
	public static class EqualsOperatorExpression extends OperatorExpression {

		@Override
		public void exec(Stack<Object> runtimeStack, Map<String, Object> valueMap) {
			Object obj0 = runtimeStack.pop();
			Object obj1 = runtimeStack.pop();
			runtimeStack.push(("" + obj0).equals("" + obj1));
		}

	}

	@Override
	public String getOperatorToken() {
		return "EQUALS";
	}

	@Override
	public OperatorExpression createOperatorExpression() {
		return new EqualsOperatorExpression();
	}

}