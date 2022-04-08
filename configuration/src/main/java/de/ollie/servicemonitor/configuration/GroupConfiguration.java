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
public class GroupConfiguration {

	private List<CheckConfiguration> checks = new ArrayList<>();
	private String name;
	private OutputConfiguration output;

}
