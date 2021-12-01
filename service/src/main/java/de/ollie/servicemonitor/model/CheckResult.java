package de.ollie.servicemonitor.model;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * @author ollie (23.11.2021)
 */
@Accessors(chain = true)
@Data
@Generated
public class CheckResult {

	public enum Status {
		OK,
		WARN,
		FAIL,
		ERROR;
	}

	private Status status;

}
