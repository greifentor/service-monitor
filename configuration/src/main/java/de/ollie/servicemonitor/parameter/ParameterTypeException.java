package de.ollie.servicemonitor.parameter;

import lombok.Getter;

@Getter
public class ParameterTypeException extends ConsoleRunnerException {

	private String expectedTypeName;
	private String parameterName;
	private String valueContent;

	public ParameterTypeException(Throwable cause, String parameterName, String valueContent,
			String expectedTypeName) {
		super(
		        "wrong type of parameter '" + parameterName
		                + "'. Expected a '"
		                + expectedTypeName
		                + "', but value does not match: '"
				+ valueContent + "'", cause);
		this.expectedTypeName = expectedTypeName;
		this.parameterName = parameterName;
		this.valueContent = valueContent;
	}

}
