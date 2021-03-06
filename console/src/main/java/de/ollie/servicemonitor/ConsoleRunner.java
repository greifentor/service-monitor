package de.ollie.servicemonitor;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.json.JsonParseException;

import de.ollie.servicemonitor.configuration.MonitoringConfiguration;
import de.ollie.servicemonitor.configuration.MonitoringConfigurationToCheckRequestGroupConverter;
import de.ollie.servicemonitor.configuration.reader.YAMLConfigurationFileReader;
import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckRequestGroup;
import de.ollie.servicemonitor.model.CheckResult;
import de.ollie.servicemonitor.model.MonitorResult;
import de.ollie.servicemonitor.model.OutputAlternative;
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

	public void run(ApplicationArguments args) {
		out.println("\n\n> application started at: " + LocalDateTime.now());
		try {
			readCallParametersFromArgs(args);
			readMonitoringConfigurationFromYAMLFile();
			convertMonitoringConfigurationToCheckRequestGroups();
			while (isCheckedForARun()) {
				out.println("\n> run started at: " + LocalDateTime.now());
				callMonitorServiceForCheckRequests();
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
		out.println("\n> Configuration:");
		out.println(String.format("> file:   %s", callParameters.getConfigurationFileNames()));
		out.println(String.format("> repeat: %s",
				callParameters.getRepeatInSeconds() != null
						? "any " + callParameters.getRepeatInSeconds() + "s"
						: "no"));
	}

	private void readMonitoringConfigurationFromYAMLFile()
			throws JsonParseException, IOException {
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
			if (callParameters.getRepeatInSeconds() != null) {
				try {
					Thread.sleep(callParameters.getRepeatInSeconds() * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return b;
	}

	private void callMonitorServiceForCheckRequests() {
		checkRequestGroups.forEach(
				checkRequestGroup -> printMonitorResultToConsole(
						monitorService.monitor(checkRequestGroup.getCheckRequests())));
	}

	private void printMonitorResultToConsole(MonitorResult monitorResult) {
		out.println();
		monitorResult.getCheckResults().forEach(checkResult -> out.println(createMessageForCheckResult(checkResult)));
	}

	private String createMessageForCheckResult(CheckResult checkResult) {
		CheckRequest checkRequest = checkResult.getCheckRequest();
		return checkRequest.getGroup()
				.getOutput()
				.getColumns()
				.stream()
				.map(outputColumn -> convertOutputColumnToString(checkResult, outputColumn, checkRequest))
				.reduce((s0, s1) -> s0 + " | " + s1)
				.orElse("n/a");
	}

	private String convertOutputColumnToString(CheckResult checkResult, OutputColumn outputColumn,
			CheckRequest checkRequest) {
		String content = findOutputAlternative(checkRequest, outputColumn.getId()).map(OutputAlternative::getContent)
				.orElse(outputColumn.getContent());
		String message = messageValueReplacer
				.getMessageWithReplacesValues(content, checkRequest, checkResult);
		return String.format(getFormatString(outputColumn), message);
	}

	private String getFormatString(OutputColumn outputColumn) {
		return "%" + (outputColumn.getAlign() == Alignment.LEFT ? "-" : "") + getOutputColumnWidth(outputColumn) + "s";
	}

	private String getOutputColumnWidth(OutputColumn outputColumn) {
		return outputColumn.getWidth() > 0 ? "" + outputColumn.getWidth() : "";
	}

	private Optional<OutputAlternative> findOutputAlternative(CheckRequest checkRequest, String id) {
		return checkRequest.getOutputAlternatives() == null
				? Optional.empty()
				: checkRequest.getOutputAlternatives()
				.stream()
				.filter(outputAlternative -> outputAlternative.getId().equals(id))
				.findFirst();
	}

}