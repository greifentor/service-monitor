package de.ollie.servicemonitor;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ollie (24.11.2021)
 */
@Accessors(chain = true)
@Data
public class CallParameters {

	private String configurationFileName;

}