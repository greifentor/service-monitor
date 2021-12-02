package de.ollie.servicemonitor.model;

import java.util.List;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * @author ollie (02.12.2021)
 */
@Accessors(chain = true)
@Data
@Generated
public class Output {

	private List<OutputColumn> columns;

}
