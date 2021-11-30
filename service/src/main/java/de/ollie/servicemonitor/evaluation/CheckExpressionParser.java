package de.ollie.servicemonitor.evaluation;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.ExecutableExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorExpressionParser;
import de.ollie.servicemonitor.evaluation.model.ValueExpression;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor
public class CheckExpressionParser {

	private final List<OperatorExpressionParser> operatorParsers;

	/**
	 * Parses the passed check expression and returns an executable List for the check expression evaluator.
	 * 
	 * @param checkExpression A string with a check expression to parse.
	 * @return An executable List for the check expression evaluator.
	 */
	public List<ExecutableExpression> parse(String checkExpression) {
		ensure(checkExpression != null, "check expression cannot be null.");
		List<ExecutableExpression> expressions = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(checkExpression, " ");
		while (st.hasMoreTokens()) {
			expressions.add(convertStringToExecutableExpression(st.nextToken()));
		}
		return expressions;
	}

	private ExecutableExpression convertStringToExecutableExpression(String s) {
		return getOperatorExpression(s).map(operator -> (ExecutableExpression) operator).orElse(convertToValue(s));
	}

	private Optional<OperatorExpression> getOperatorExpression(String s) {
		return operatorParsers
				.stream()
				.filter(operatorParser -> operatorParser.getOperatorToken().equals(s))
				.findFirst()
				.map(OperatorExpressionParser::createOperatorExpression);
	}

	private ValueExpression convertToValue(String s) {
		try {
			return new ValueExpression().setValue(Long.parseLong(s));
		} catch (Exception e) {
		}
		return new ValueExpression().setValue(s);
	}

}
