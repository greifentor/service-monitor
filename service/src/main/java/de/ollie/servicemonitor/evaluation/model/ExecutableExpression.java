package de.ollie.servicemonitor.evaluation.model;

public interface ExecutableExpression {

	public enum Type {
		OPERATOR,
		VALUE;
	}

	Type getType();

}