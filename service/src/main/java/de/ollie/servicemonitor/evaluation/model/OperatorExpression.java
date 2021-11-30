package de.ollie.servicemonitor.evaluation.model;

import java.util.Map;
import java.util.Stack;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class OperatorExpression implements ExecutableExpression {

	@Override
	public Type getType() {
		return Type.OPERATOR;
	}

	public abstract void exec(Stack<Object> runtimeStack, Map<String, Object> valueMap);

}