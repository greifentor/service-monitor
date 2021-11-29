package de.ollie.servicemonitor.evaluation.parser;

import java.util.Stack;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.ExecutableExpression;
import de.ollie.servicemonitor.evaluation.model.Operator;
import de.ollie.servicemonitor.evaluation.model.OperatorParser;
import lombok.EqualsAndHashCode;

@Named
public class EqualsOperatorParser implements OperatorParser {

	@EqualsAndHashCode
	public static class EqualsOperator implements Operator {

		@Override
		public void exec(Stack<ExecutableExpression> runtimeStack) {
			// TODO OLI: Implementation.
		}

	}

	@Override
	public String getOperatorToken() {
		return "EQUALS";
	}

	@Override
	public Operator createOperator() {
		return new EqualsOperator();
	}

}