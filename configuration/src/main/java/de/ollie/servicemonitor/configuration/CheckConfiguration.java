package de.ollie.servicemonitor.configuration;

import java.util.ArrayList;
import java.util.List;

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

	private String authenticationBearer;
	private String checkExpression;
	private String host;
	private boolean https;
	private String name;
	private List<OutputAlternativesConfiguration> outputAlternatives = new ArrayList<>();
	private String path;
	private Integer port;
	private ReturnType returnType;

}
