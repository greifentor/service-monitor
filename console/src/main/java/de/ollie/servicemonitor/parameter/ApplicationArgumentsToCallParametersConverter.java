package de.ollie.servicemonitor.parameter;

import static de.ollie.servicemonitor.util.Check.ensure;

import javax.inject.Named;

import org.springframework.boot.ApplicationArguments;

import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor
public class ApplicationArgumentsToCallParametersConverter {

	public static final String FILE_OPTION_NAME = "file";

	public CallParameters convert(ApplicationArguments args) {
		ensure(args != null, "args cannot ne null.");
		ensure(args.containsOption(FILE_OPTION_NAME),
				new MissingOptionException("missing option with name: " + FILE_OPTION_NAME, FILE_OPTION_NAME));
		return new CallParameters().setConfigurationFileNames(args.getOptionValues(FILE_OPTION_NAME));
	}

}
