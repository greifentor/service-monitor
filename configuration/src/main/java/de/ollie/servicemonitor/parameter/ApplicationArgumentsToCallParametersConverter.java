package de.ollie.servicemonitor.parameter;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.List;

import javax.inject.Named;

import org.springframework.boot.ApplicationArguments;

@Named
public class ApplicationArgumentsToCallParametersConverter {

	public static final String FILE_OPTION_NAME = "file";
	public static final String FONT_SIZE_OPTION_NAME = "fontSize";
	public static final String REPEAT_IN_SECONDS_OPTION_NAME = "repeatInSeconds";

	public CallParameters convert(ApplicationArguments args) {
		ensure(args != null, "args cannot ne null.");
		ensure(args.containsOption(FILE_OPTION_NAME), new MissingOptionException(FILE_OPTION_NAME));
		List<String> fileNames = args.getOptionValues(FILE_OPTION_NAME);
		int fontSize = getFontSizeFromArguments(args) != null ? getFontSizeFromArguments(args) : 4;
		Long repeatInSeconds = getRepeatInSecondsFromArguments(args);
		return new CallParameters()
		        .setConfigurationFileNames(fileNames)
		        .setFontSize(fontSize)
		        .setRepeatInSeconds(repeatInSeconds);
	}

	private Integer getFontSizeFromArguments(ApplicationArguments args) {
		if (!args.containsOption(FONT_SIZE_OPTION_NAME)) {
			return null;
		}
		String valueStr = args.getOptionValues(FONT_SIZE_OPTION_NAME).get(0);
		Integer value = null;
		try {
			value = Integer.parseInt(valueStr);
		} catch (Exception e) {
			throw new ParameterTypeException(e, FONT_SIZE_OPTION_NAME, valueStr, "Integer");
		}
		if ((value < 0) || (value > 7)) {
			throw new IllegalValueException(FONT_SIZE_OPTION_NAME, value, "1..7");
		}
		return value;
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
