package de.ollie.servicemonitor;

import java.awt.EventQueue;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import de.ollie.servicemonitor.swing.SwingRunner;

/**
 * @author ollie (16.02.2022)
 */
@SpringBootApplication
@ComponentScan("de.ollie")
public class SwingRunnerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =
		        new SpringApplicationBuilder(SwingRunner.class).headless(false).run(args);
		EventQueue.invokeLater(() -> {
			SwingRunner ex = ctx.getBean(SwingRunner.class);
			// TODO OLI: Think about passing the command line parameters here ... would be less effort in configuration
			// ...
			ex.setVisible(true);
		});
	}

}