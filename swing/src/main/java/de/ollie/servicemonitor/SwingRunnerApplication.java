package de.ollie.servicemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ollie (16.02.2022)
 */
@SpringBootApplication
@ComponentScan("de.ollie")
public class SwingRunnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwingRunnerApplication.class, args);
	}

}