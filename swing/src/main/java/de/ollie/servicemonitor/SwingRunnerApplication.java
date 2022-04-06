package de.ollie.servicemonitor;

import java.awt.EventQueue;

import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import de.ollie.servicemonitor.parameter.ApplicationArgumentsToCallParametersConverter;

/**
 * @author ollie (16.02.2022)
 */
@SpringBootApplication
@ComponentScan("de.ollie")
public class SwingRunnerApplication {

	public static void main(String[] args) {
		var ctx = new SpringApplicationBuilder(SwingRunnerApplication.class).headless(false).run(args);
		EventQueue.invokeLater(() -> {
			var ex0 = ctx.getBean(SwingRunner.class);
			ex0
			        .setCallParameters(
			                new ApplicationArgumentsToCallParametersConverter()
			                        .convert(new DefaultApplicationArguments(args)));
			ex0.buildComponents();
			ex0.setVisible(true);
		});
	}

}