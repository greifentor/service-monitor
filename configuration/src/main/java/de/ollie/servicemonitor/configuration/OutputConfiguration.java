package de.ollie.servicemonitor.configuration;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ollie (02.12.2021)
 */
@Accessors(chain = true)
@Data
public class OutputConfiguration {

	private List<OutputColumnConfiguration> columns;

}
