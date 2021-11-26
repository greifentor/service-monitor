package de.ollie.servicemonitor.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ollie (23.11.2021)
 */
@Accessors(chain = true)
@Data
public class CheckRequest {

	private String name;
	private String url;
	private String checkExpression;

}
