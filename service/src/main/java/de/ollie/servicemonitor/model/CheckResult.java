package de.ollie.servicemonitor.model;

import java.util.Map;

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

	private CheckRequestGroup group;
	private CheckRequest checkRequest;
	private String errorMessage;
	private String name;
	private Status status;
	private Map<String, Object> valueMap;

}
