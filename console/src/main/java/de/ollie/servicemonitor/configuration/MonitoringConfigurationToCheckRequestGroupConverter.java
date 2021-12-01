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
		return new CheckRequestGroup()
				.setCheckRequests(convertCheckConfigurationList(groupConfiguration.getChecks()))
				.setName(groupConfiguration.getName());
	}

	private List<CheckRequest> convertCheckConfigurationList(List<CheckConfiguration> checkConfigurations) {
		return checkConfigurations
				.stream()
				.map(checkConfiguration -> convertCheckConfiguration(checkConfiguration))
				.collect(Collectors.toList());
	}

	private CheckRequest convertCheckConfiguration(CheckConfiguration checkConfiguration) {
		ensure(checkConfiguration.getHost() != null, new IllegalStateException("host cannot be null."));
		return new CheckRequest()
				.setCheckExpression(checkConfiguration.getCheckExpression())
				.setHost(checkConfiguration.getHost())
				.setMessage(checkConfiguration.getMessage())
				.setName(checkConfiguration.getName())
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

}
