package de.ollie.servicemonitor.evaluation.parser;

import java.util.Stack;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.ExecutableExpression;
import de.ollie.servicemonitor.evaluation.model.Operator;
import de.ollie.servicemonitor.evaluation.model.OperatorParser;
import lombok.EqualsAndHashCode;

@Named
public class ReadValueOperatorParser implements OperatorParser {

	@EqualsAndHashCode
	public static class ReadValueOperator implements Operator {

		@Override
		public void exec(Stack<ExecutableExpression> runtimeStack) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Implementierung fehlt");
		}

	}

	@Override
	public String getOperatorToken() {
		return "READ_VALUE";
	}

	@Override
	public Operator createOperator() {
		return new ReadValueOperator();
	}

}