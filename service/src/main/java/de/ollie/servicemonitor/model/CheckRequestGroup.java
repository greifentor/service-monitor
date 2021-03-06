package de.ollie.servicemonitor.model;

import java.util.List;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * @author ollie (26.11.2021)
 */
@Accessors(chain = true)
@Data
@Generated
public class CheckRequestGroup {

	private List<CheckRequest> checkRequests;
	private String name;
	private Output output;

}
