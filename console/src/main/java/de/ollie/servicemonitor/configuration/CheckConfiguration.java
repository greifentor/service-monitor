package de.ollie.servicemonitor.configuration;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ollie (24.11.2021)
 */
@Accessors(chain = true)
@Data
public class CheckConfiguration {

	public enum ReturnType {
		JSON,
		STRING,
		XML;
	}

	private String name;
	private String host;
	private Integer port;
	private String path;
	private ReturnType returnType;
	private String checkExpression;

}
