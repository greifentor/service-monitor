package de.ollie.servicemonitor.configuration.reader;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import org.springframework.boot.json.JsonParseException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.ollie.servicemonitor.configuration.MonitoringConfiguration;

/**
 * @author ollie (24.11.2021)
 */
@Named
public class YAMLConfigurationFileReader {

	/**
	 * Reads the configuration for the monitoring from a YAML file.
	 * 
	 * @param fileName The name of the YAML file to read.
	 * @return A configuration object with the data of the YAML file.
	 * @throws IOException          If something went wrong while accessing the file system.
	 * @throws JsonMappingException If an error occurs while mapping the data.
	 * @throws JsonParseException   On error while parsing the file.
	 */
	public MonitoringConfiguration read(String fileName) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		return mapper.readValue(new File(fileName), MonitoringConfiguration.class);
	}

}