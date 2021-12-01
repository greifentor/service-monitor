package de.ollie.servicemonitor.evaluation.parser;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.inject.Named;

import de.ollie.servicemonitor.evaluation.model.OperatorExpression;
import de.ollie.servicemonitor.evaluation.model.OperatorExpressionParser;

@Named
public class ReadValueOperatorParser implements OperatorExpressionParser {

	public static class ReadValueOperatorExpression extends OperatorExpression {

		@Override
		public void exec(Stack<Object> runtimeStack, Map<String, Object> valueMap) {
			String path = "" + runtimeStack.pop();
			StringTokenizer pathElements = new StringTokenizer(path, ".");
			Object o = null;
			while (pathElements.hasMoreTokens()) {
				String element = pathElements.nextToken();
				o = valueMap.get(element);
				if (o == null) {
					throw new NoSuchElementException("no path found for: " + path + ", element: " + element);
				}
				if (o instanceof Map) {
					valueMap = (Map) o;
				}
			}
			runtimeStack.push(o);
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