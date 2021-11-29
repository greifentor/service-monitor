package de.ollie.servicemonitor.evaluation;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.Operator;
import de.ollie.servicemonitor.evaluation.model.OperatorParser;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor
public class CheckExpressionParser {

	private final List<OperatorParser> operatorParsers;

	/**
	 * Parses the passed check expression and returns an executable List for the check expression evaluator.
	 * 
	 * @param checkExpression A string with a check expression to parse.
	 * @return An executable List for the check expression evaluator.
	 */
	public Stack<Object> parse(String checkExpression) {
		ensure(checkExpression != null, "check expression cannot be null.");
		Stack<Object> expressions = new Stack<>();
		StringTokenizer st = new StringTokenizer(checkExpression, " ");
		while (st.hasMoreTokens()) {
			expressions.add(convertStringToExecutableExpression(st.nextToken()));
		}
		return expressions;
	}

	private Object convertStringToExecutableExpression(String s) {
		return getOperator(s).map(operator -> (Object) operator).orElse(convertToValue(s));
	}

	private Optional<Operator> getOperator(String s) {
		return operatorParsers
				.stream()
				.filter(operatorParser -> operatorParser.getOperatorToken().equals(s))
				.findFirst()
				.map(OperatorParser::createOperator);
	}

	private Object convertToValue(String s) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
		}
		return s;
	}

}
