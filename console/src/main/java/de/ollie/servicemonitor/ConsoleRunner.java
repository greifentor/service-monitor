package de.ollie.servicemonitor;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.json.JsonParseException;

import com.fasterxml.jackson.databind.JsonMappingException;

import de.ollie.servicemonitor.configuration.MonitoringConfiguration;
import de.ollie.servicemonitor.configuration.reader.YAMLConfigurationFileReader;
import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.MonitorResult;
import de.ollie.servicemonitor.parameter.ApplicationArgumentsToCallParametersConverter;
import de.ollie.servicemonitor.parameter.CallParameters;
import lombok.RequiredArgsConstructor;

/**
 * @author ollie (24.11.2021)
 */
@RequiredArgsConstructor
public class ConsoleRunner {

	private final ApplicationArgumentsToCallParametersConverter applicationArgumentsToCallParametersConverter;
	private final YAMLConfigurationFileReader yamlConfigurationFileReader;

	private CallParameters callParameters;
	private List<CheckRequest> checkRequests;
	private MonitoringConfiguration monitoringConfiguration;
	private MonitorResult monitorResult;

	public void run(ApplicationArguments args) {
		try {
			readCallParametersFromArgs(args);
			readMonitoringConfigurationFromYAMLFile();
			convertMonitoringConfigurationToCheckRequestList();
			callMonitorServiceForCheckRequests();
			printMonitorResultToConsole();
		} catch (Exception e) {
			// TODO error message output.
		}
	}

	private void readCallParametersFromArgs(ApplicationArguments args) {
		callParameters = applicationArgumentsToCallParametersConverter.convert(args);
	}

	private void readMonitoringConfigurationFromYAMLFile()
			throws JsonMappingException, JsonParseException, IOException {
		monitoringConfiguration = yamlConfigurationFileReader.read(callParameters.getConfigurationFileNames().get(0));
	}

	private void convertMonitoringConfigurationToCheckRequestList() {
		checkRequests = null;
	}

	private void callMonitorServiceForCheckRequests() {
		monitorResult = null;
	}

	private void printMonitorResultToConsole() {
	}

}