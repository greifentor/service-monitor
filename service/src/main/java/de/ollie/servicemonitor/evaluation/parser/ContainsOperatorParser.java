package de.ollie.servicemonitor.evaluation.parser;

import java.util.Map;
import java.util.Stack;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.OperatorExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorExpressionParser;

@Named
public class ContainsOperatorParser implements OperatorExpressionParser {

	public static class ContainsOperatorExpression extends OperatorExpression {

		@Override
		public void exec(Stack<Object> runtimeStack, Map<String, Object> valueMap) {
			Object obj0 = runtimeStack.pop();
			Object obj1 = runtimeStack.pop();
			runtimeStack.push(("" + obj1).contains("" + obj0));
		}

	}

	@Override
	public String getOperatorToken() {
		return "CONTAINS";
	}

	@Override
	public OperatorExpression createOperatorExpression() {
		return new ContainsOperatorExpression();
	}

}