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
import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckRequestGroup;
import de.ollie.servicemonitor.model.CheckResult;
import de.ollie.servicemonitor.model.MonitorResult;
import de.ollie.servicemonitor.model.OutputColumn;
import de.ollie.servicemonitor.model.OutputColumn.Alignment;
import de.ollie.servicemonitor.parameter.ApplicationArgumentsToCallParametersConverter;
import de.ollie.servicemonitor.parameter.CallParameters;
import de.ollie.servicemonitor.parameter.ConsoleRunnerException;
import lombok.RequiredArgsConstructor;

/**
 * @author ollie (24.11.2021)
 */
@RequiredArgsConstructor
public class ConsoleRunner {

	private final ApplicationArgumentsToCallParametersConverter applicationArgumentsToCallParametersConverter;
	private final MessageValueReplacer messageValueReplacer;
	private final MonitorService monitorService;
	private final MonitoringConfigurationToCheckRequestGroupConverter monitoringConfigurationToCheckRequestGroupConverter;
	private final YAMLConfigurationFileReader yamlConfigurationFileReader;
	private final PrintStream out;

	private boolean firstRunDone = false;
	private CallParameters callParameters;
	private List<CheckRequestGroup> checkRequestGroups;
	private MonitoringConfiguration monitoringConfiguration;
	private MonitorResult monitorResult;

	public void run(ApplicationArguments args) {
		out.println("\n\n> application started at: " + LocalDateTime.now());
		try {
			readCallParametersFromArgs(args);
			readMonitoringConfigurationFromYAMLFile();
			convertMonitoringConfigurationToCheckRequestGroups();
			while (isCheckedForARun()) {
				out.println("\n> run started at: " + LocalDateTime.now());
				callMonitorServiceForCheckRequests();
				printMonitorResultToConsole();
				out.println("\n> run finished started at: " + LocalDateTime.now());
			}
		} catch (ConsoleRunnerException e) {
			out.println("\nERROR> " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("\n> application finished at: " + LocalDateTime.now());
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

	private boolean isCheckedForARun() {
		boolean b = (callParameters.getRepeatInSeconds() != null) || !firstRunDone;
		if (!firstRunDone) {
			firstRunDone = true;
		} else {
			try {
				Thread.sleep(callParameters.getRepeatInSeconds() * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	private void callMonitorServiceForCheckRequests() {
		checkRequestGroups.forEach(
				checkRequestGroup -> monitorResult = monitorService.monitor(checkRequestGroup.getCheckRequests()));
	}

	private void printMonitorResultToConsole() {
		out.println();
		monitorResult.getCheckResults().forEach(checkResult -> out.println(createMessageForCheckResult(checkResult)));
	}

	private String createMessageForCheckResult(CheckResult checkResult) {
		CheckRequest checkRequest = checkResult.getCheckRequest();
		return checkRequest.getOutput()
				.getColumns()
				.stream()
				.map(outputColumn -> convertOutputColumnToString(checkResult, outputColumn, checkRequest))
				.reduce((s0, s1) -> s0 + " | " + s1)
				.orElse("n/a");
	}

	private String convertOutputColumnToString(CheckResult checkResult, OutputColumn outputColumn,
			CheckRequest checkRequest) {
		String message = messageValueReplacer
				.getMessageWithReplacesValues(outputColumn.getContent(), checkRequest, checkResult);
		return String.format("%" + (outputColumn.getAlign() == Alignment.LEFT ? "-" : "")
				+ (outputColumn.getWidth() > 0 ? outputColumn.getWidth() : "") + "s", message);
	}

}