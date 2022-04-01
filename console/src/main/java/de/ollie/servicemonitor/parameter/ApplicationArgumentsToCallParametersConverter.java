package de.ollie.servicemonitor.parameter;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.List;

import javax.inject.Named;

import org.springframework.boot.ApplicationArguments;

@Named
public class ApplicationArgumentsToCallParametersConverter {

	public static final String FILE_OPTION_NAME = "file";
	public static final String REPEAT_IN_SECONDS_OPTION_NAME = "repeatInSeconds";

	public CallParameters convert(ApplicationArguments args) {
		ensure(args != null, "args cannot ne null.");
		ensure(args.containsOption(FILE_OPTION_NAME), new MissingOptionException(FILE_OPTION_NAME));
		List<String> fileNames = args.getOptionValues(FILE_OPTION_NAME);
		Long repeatInSeconds = getRepeatInSecondsFromArguments(args);
		return new CallParameters().setConfigurationFileNames(fileNames).setRepeatInSeconds(repeatInSeconds);
	}

	private Long getRepeatInSecondsFromArguments(ApplicationArguments args) {
		if (!args.containsOption(REPEAT_IN_SECONDS_OPTION_NAME)) {
			return null;
		}
		String valueStr = args.getOptionValues(REPEAT_IN_SECONDS_OPTION_NAME).get(0);
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
