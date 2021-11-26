package de.ollie.servicemonitor.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ollie (26.11.2021)
 */
@Accessors(chain = true)
@Data
public class CheckRequestGroup {

	private String name;
	private List<CheckRequest> checkRequests;

}
