package de.ollie.servicemonitor.parameter;

import lombok.Getter;

@Getter
public class MissingOptionException extends ConsoleRunnerException {

	private String optionName;

	public MissingOptionException(String optionName) {
		super("missing option with name: " + optionName);
		this.optionName = optionName;
	}

}