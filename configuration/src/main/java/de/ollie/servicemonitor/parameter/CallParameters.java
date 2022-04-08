package de.ollie.servicemonitor.parameter;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ollie (24.11.2021)
 */
@Accessors(chain = true)
@Data
public class CallParameters {

	private List<String> configurationFileNames;
	private int fontSize = 4;
	private Long repeatInSeconds;

}