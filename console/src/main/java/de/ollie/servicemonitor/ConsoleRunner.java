package de.ollie.servicemonitor;

import java.util.List;

import javax.inject.Named;

import org.springframework.boot.ApplicationArguments;

import de.ollie.servicemonitor.configuration.MonitoringConfiguration;
import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.MonitorResult;
import de.ollie.servicemonitor.parameter.CallParameters;

/**
 * @author ollie (24.11.2021)
 */
@Named
public class ConsoleRunner {

	public void run(ApplicationArguments args) {
		CallParameters callParameters = readCallParametersFromArgs(args);
		MonitoringConfiguration monitoringConfiguration = readMonitoringConfigurationFromYAMLFile(callParameters);
		List<CheckRequest> checkRequests = convertMonitoringConfigurationToCheckRequestList(monitoringConfiguration);
		MonitorResult monitorResult = callMonitorServiceForCheckRequests(checkRequests);
		printMonitorResultToConsole(monitorResult);
	}

	private CallParameters readCallParametersFromArgs(ApplicationArguments args) {
		return null;
	}

	private MonitoringConfiguration readMonitoringConfigurationFromYAMLFile(CallParameters callParameters) {
		return null;
	}

	private List<CheckRequest> convertMonitoringConfigurationToCheckRequestList(
			MonitoringConfiguration monitoringConfiguration) {
		return null;
	}

	private MonitorResult callMonitorServiceForCheckRequests(List<CheckRequest> checkRequests) {
		return null;
	}

	private void printMonitorResultToConsole(MonitorResult monitorResult) {
	}

}