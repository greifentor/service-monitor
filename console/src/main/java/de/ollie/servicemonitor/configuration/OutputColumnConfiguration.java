package de.ollie.servicemonitor.configuration;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ollie (02.12.2021)
 */
@Accessors(chain = true)
@Data
public class OutputColumnConfiguration {

	public enum Alignment {
		CENTER,
		LEFT,
		RIGHT;
	}

	private Alignment align;
	private String content;
	private String name;
	private Integer width;

}
