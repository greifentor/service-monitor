package de.ollie.servicemonitor.evaluation.parser;

import java.util.Stack;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.ExecutableExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorParser;
import lombok.EqualsAndHashCode;

@Named
public class ReadValueOperatorParser implements OperatorParser {

	@EqualsAndHashCode(callSuper = true)
	public static class ReadValueOperatorExpression extends OperatorExpression {

		@Override
		public void execute(Stack<ExecutableExpression> runtimeStack) {
			// TODO OLI: Implementation.
		}

	}

	@Override
	public String getOperatorToken() {
		return "READ_VALUE";
	}

	@Override
	public OperatorExpression createOperatorExpression() {
		return new ReadValueOperatorExpression();
	}

}