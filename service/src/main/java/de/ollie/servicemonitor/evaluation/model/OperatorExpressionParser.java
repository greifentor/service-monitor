package de.ollie.servicemonitor.evaluation.model;

public interface OperatorExpressionParser {

	String getOperatorToken();

	OperatorExpression createOperatorExpression();

}