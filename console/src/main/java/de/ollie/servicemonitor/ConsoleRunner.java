package de.ollie.servicemonitor;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.json.JsonParseException;

import com.fasterxml.jackson.databind.JsonMappingException;

import de.ollie.servicemonitor.configuration.MonitoringConfiguration;
import de.ollie.servicemonitor.configuration.MonitoringConfigurationToCheckRequestGroupConverter;
import de.ollie.servicemonitor.configuration.reader.YAMLConfigurationFileReader;
import de.ollie.servicemonitor.model.CheckRequestGroup;
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
	private final MonitorService monitorService;
	private final MonitoringConfigurationToCheckRequestGroupConverter monitoringConfigurationToCheckRequestGroupConverter;
	private final YAMLConfigurationFileReader yamlConfigurationFileReader;
	private final PrintStream out;

	private CallParameters callParameters;
	private List<CheckRequestGroup> checkRequestGroups;
	private MonitoringConfiguration monitoringConfiguration;
	private MonitorResult monitorResult;

	public void run(ApplicationArguments args) {
		try {
			out.println("\n\n> run started at: " + LocalDateTime.now());
			readCallParametersFromArgs(args);
			readMonitoringConfigurationFromYAMLFile();
			convertMonitoringConfigurationToCheckRequestGroups();
			callMonitorServiceForCheckRequests();
			printMonitorResultToConsole();
			out.println("\n> run finished at: " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readCallParametersFromArgs(ApplicationArguments args) {
		callParameters = applicationArgumentsToCallParametersConverter.convert(args);
	}

	private void readMonitoringConfigurationFromYAMLFile()
			throws JsonMappingException, JsonParseException, IOException {
		monitoringConfiguration = yamlConfigurationFileReader.read(callParameters.getConfigurationFileNames().get(0));
	}

	private void convertMonitoringConfigurationToCheckRequestGroups() {
		checkRequestGroups = monitoringConfigurationToCheckRequestGroupConverter.convert(monitoringConfiguration);
	}

	private void callMonitorServiceForCheckRequests() {
		checkRequestGroups
				.forEach(
						checkRequestGroup -> monitorResult =
								monitorService.monitor(checkRequestGroup.getCheckRequests()));
	}

	private void printMonitorResultToConsole() {
		out.println("\n" + monitorResult);
	}

}