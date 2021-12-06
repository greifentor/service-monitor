package de.ollie.servicemonitor.configuration;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import de.ollie.servicemonitor.configuration.CheckConfiguration.ReturnType;
import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckRequest.ReturnedMediaType;
import de.ollie.servicemonitor.model.CheckRequestGroup;
/**
 * @author ollie (26.11.2021)
 */
import de.ollie.servicemonitor.model.Output;
import de.ollie.servicemonitor.model.OutputAlternative;
import de.ollie.servicemonitor.model.OutputColumn;
import de.ollie.servicemonitor.model.OutputColumn.Alignment;
@Named
public class MonitoringConfigurationToCheckRequestGroupConverter {

	public List<CheckRequestGroup> convert(MonitoringConfiguration monitoringConfiguration) {
		if (monitoringConfiguration == null) {
			return null;
		}
		List<CheckRequestGroup> result = new ArrayList<>();
		monitoringConfiguration
				.getGroups()
				.forEach(groupConfiguration -> result.add(convertGroupConfiguration(groupConfiguration)));
		return result;
	}

	private CheckRequestGroup convertGroupConfiguration(GroupConfiguration groupConfiguration) {
		CheckRequestGroup group = new CheckRequestGroup();
		return group.setCheckRequests(convertCheckConfigurationList(group, groupConfiguration.getChecks()))
				.setName(groupConfiguration.getName())
				.setOutput(convertOutputConfigurationToOutput(groupConfiguration.getOutput()));
	}

	private List<CheckRequest> convertCheckConfigurationList(CheckRequestGroup group,
			List<CheckConfiguration> checkConfigurations) {
		return checkConfigurations
				.stream()
				.map(checkConfiguration -> convertCheckConfiguration(group, checkConfiguration))
				.collect(Collectors.toList());
	}

	private CheckRequest convertCheckConfiguration(CheckRequestGroup group, CheckConfiguration checkConfiguration) {
		ensure(checkConfiguration.getHost() != null, new IllegalStateException("host cannot be null."));
		return new CheckRequest()
				.setAuthenticationBearer(checkConfiguration.getAuthenticationBearer())
				.setCheckExpression(checkConfiguration.getCheckExpression())
				.setGroup(group)
				.setHost(checkConfiguration.getHost())
				.setName(checkConfiguration.getName())
				.setOutputAlternatives(getOutputAlternatives(checkConfiguration.getOutputAlternatives()))
				.setPath(checkConfiguration.getPath())
				.setPort(checkConfiguration.getPort())
				.setReturnedMediaType(convertReturnTypeToReturnedMediaType(checkConfiguration.getReturnType()));
	}

	private ReturnedMediaType convertReturnTypeToReturnedMediaType(ReturnType returnType) {
		if (returnType == null) {
			return ReturnedMediaType.STRING;
		}
		switch (returnType) {
		case JSON:
			return ReturnedMediaType.JSON;
		case XML:
			return ReturnedMediaType.XML;
		default:
			return ReturnedMediaType.STRING;
		}
	}

	private Output convertOutputConfigurationToOutput(OutputConfiguration output) {
		if (output == null) {
			return null;
		}
		return new Output().setColumns(convertOutputColumnConfigurationsToOutputColumns(output.getColumns()));
	}

	private List<OutputColumn> convertOutputColumnConfigurationsToOutputColumns(List<OutputColumnConfiguration> outputColumnConfigurations) {
		return outputColumnConfigurations.stream()
				.map(outputColumnConfiguration -> convertOutputColumnConfigurationToOutputColumn(
						outputColumnConfiguration))
				.collect(Collectors.toList());
	}

	private OutputColumn convertOutputColumnConfigurationToOutputColumn(
			OutputColumnConfiguration outputColumnConfiguration) {
		return new OutputColumn().setAlign(Alignment.valueOf(outputColumnConfiguration.getAlign().name()))
				.setContent(outputColumnConfiguration.getContent())
				.setId(outputColumnConfiguration.getId())
				.setName(outputColumnConfiguration.getName())
				.setWidth(outputColumnConfiguration.getWidth());
	}

	private List<OutputAlternative> getOutputAlternatives(
			List<OutputAlternativesConfiguration> outputAlternativConfigurations) {
		return outputAlternativConfigurations.stream()
				.map(outputAlternativConfiguration -> getOutputAlternative(outputAlternativConfiguration))
				.collect(Collectors.toList());
	}

	private OutputAlternative getOutputAlternative(OutputAlternativesConfiguration outputAlternativConfiguration) {
		return new OutputAlternative().setContent(outputAlternativConfiguration.getContent())
				.setId(outputAlternativConfiguration.getId());
	}

}
