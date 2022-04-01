package de.ollie.servicemonitor.swing.parameter;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.inject.Named;

import de.ollie.servicemonitor.parameter.CallParameters;
import de.ollie.servicemonitor.parameter.IllegalValueException;
import de.ollie.servicemonitor.parameter.MissingOptionException;
import de.ollie.servicemonitor.parameter.ParameterTypeException;

@Named
public class PropertiesToCallParameterConverter {

	public static final String FILE_OPTION_NAME = "file";
	public static final String REPEAT_IN_SECONDS_OPTION_NAME = "repeatInSeconds";

	public CallParameters convert(Properties properties) {
		ensure(properties != null, "properties cannot ne null.");
		List<String> fileNames = getFileNames(properties);
		Long repeatInSeconds = getRepeatInSecondsFromArguments(properties);
		return new CallParameters().setConfigurationFileNames(fileNames).setRepeatInSeconds(repeatInSeconds);
	}

	private List<String> getFileNames(Properties properties) {
		ensure(properties.containsKey(FILE_OPTION_NAME), new MissingOptionException(FILE_OPTION_NAME));
		List<String> l = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(properties.getProperty(FILE_OPTION_NAME), ",");
		while (st.hasMoreTokens()) {
			l.add(st.nextToken());
		}
		return l;
	}

	private Long getRepeatInSecondsFromArguments(Properties properties) {
		if (!properties.containsKey(REPEAT_IN_SECONDS_OPTION_NAME)) {
			return null;
		}
		String valueStr = properties.getProperty(REPEAT_IN_SECONDS_OPTION_NAME);
		Long value = null;
		try {
			value = Long.parseLong(valueStr);
		} catch (Exception e) {
			throw new ParameterTypeException(e, REPEAT_IN_SECONDS_OPTION_NAME, valueStr, "Long");
		}
		if (value < 0) {
			throw new IllegalValueException(REPEAT_IN_SECONDS_OPTION_NAME, value, "0..Long.MAX_VALUE");
		}
		return value;
	}

}