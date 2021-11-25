package de.ollie.servicemonitor.parameter;

import lombok.Getter;

@Getter
public class MissingOptionException extends RuntimeException {

	private String optionName;

	public MissingOptionException(String message, String optionName) {
		super(message);
		this.optionName = optionName;
	}

}