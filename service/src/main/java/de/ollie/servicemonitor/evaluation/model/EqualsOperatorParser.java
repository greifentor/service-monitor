package de.ollie.servicemonitor.evaluation.model;

import java.util.Stack;

import javax.inject.Named;

import lombok.EqualsAndHashCode;

@Named
public class EqualsOperatorParser implements OperatorParser {

	@EqualsAndHashCode(callSuper = true)
	public static class EqualsOperatorExpression extends OperatorExpression {

		@Override
		public void execute(Stack<ExecutableExpression> runtimeStack) {
			// TODO OLI: Implementation.
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