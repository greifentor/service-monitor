package de.ollie.servicemonitor.evaluation.model;

import java.util.Stack;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class OperatorExpression implements ExecutableExpression {

	@Override
	public Type getType() {
		return Type.OPERATOR;
	}

	public abstract void execute(Stack<ExecutableExpression> runtimeStack);

}