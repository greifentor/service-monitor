package de.ollie.servicemonitor.evaluation.model;

public interface OperatorParser {

	String getOperatorToken();

	Operator createOperator();

}