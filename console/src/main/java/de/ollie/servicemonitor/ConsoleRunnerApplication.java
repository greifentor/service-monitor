package de.ollie.servicemonitor;

import javax.inject.Inject;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import de.ollie.servicemonitor.configuration.MonitoringConfigurationToCheckRequestGroupConverter;
import de.ollie.servicemonitor.configuration.reader.YAMLConfigurationFileReader;
import de.ollie.servicemonitor.parameter.ApplicationArgumentsToCallParametersConverter;

/**
 * @author ollie (23.11.2021)
 */
@SpringBootApplication
@ComponentScan("de.ollie")
public class ConsoleRunnerApplication implements ApplicationRunner {

	@Inject
	private ApplicationArgumentsToCallParametersConverter applicationArgumentsToCallParametersConverter;
	@Inject
	private MonitoringConfigurationToCheckRequestGroupConverter monitoringConfigurationToCheckRequestGroupConverter;
	@Inject
	private YAMLConfigurationFileReader yamlConfigurationFileReader;

	public static void main(String[] args) {
		SpringApplication.run(ConsoleRunnerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		new ConsoleRunner(
				applicationArgumentsToCallParametersConverter, monitoringConfigurationToCheckRequestGroupConverter,
				yamlConfigurationFileReader, System.out)
				.run(args);
	}

}