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
public class MonitoringConfiguration {

	private List<GroupConfiguration> groups = new ArrayList<>();

}