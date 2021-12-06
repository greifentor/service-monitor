package de.ollie.servicemonitor.model;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * @author ollie (02.12.2021)
 */
@Accessors(chain = true)
@Data
@Generated
public class OutputColumn {

	public enum Alignment {
		CENTER,
		LEFT,
		RIGHT;
	}

	private Alignment align;
	private String content;
	private String id;
	private String name;
	private Integer width;

}