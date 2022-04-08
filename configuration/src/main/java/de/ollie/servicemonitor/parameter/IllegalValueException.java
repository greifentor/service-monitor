package de.ollie.servicemonitor.parameter;

import lombok.Getter;

@Getter
public class IllegalValueException extends ConsoleRunnerException {

	private String expectedValueBounds;
	private String parameterName;
	private Object valueContent;

	public IllegalValueException(String parameterName, Object valueContent,
			String expectedValueBounds) {
		super(
		        "value '" + valueContent
		                + "' for parameter '"
		                + parameterName
		                + "' is out of range: "
		                + expectedValueBounds);
		this.expectedValueBounds = expectedValueBounds;
		this.parameterName = parameterName;
		this.valueContent = valueContent;
	}

}
