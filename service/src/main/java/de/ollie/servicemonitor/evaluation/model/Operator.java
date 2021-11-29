package de.ollie.servicemonitor.evaluation.model;

import java.util.Stack;

public interface Operator {

	void exec(Stack<ExecutableExpression> runtimeStack);

}