package de.ollie.servicemonitor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class MonitorConfiguration {

	@Value("${app.version}")
	private String version;

}