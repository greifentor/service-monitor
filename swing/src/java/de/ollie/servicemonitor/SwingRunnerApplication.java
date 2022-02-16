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
 * @author ollie (16.02.2022)
 */
@SpringBootApplication
@ComponentScan("de.ollie")
public class SwingRunnerApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SwingRunnerApplication.class, args);
	}

}