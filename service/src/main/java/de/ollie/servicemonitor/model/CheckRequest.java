package de.ollie.servicemonitor.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ollie (23.11.2021)
 */
@Accessors(chain = true)
@Data
public class CheckRequest {

	public enum ReturnedMediaType {
		JSON,
		STRING,
		XML;
	}

	private String name;
	private String url;
	private ReturnedMediaType returnedMediaType;
	private String checkExpression;

}
