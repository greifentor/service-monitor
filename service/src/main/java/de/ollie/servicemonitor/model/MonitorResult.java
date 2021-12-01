package de.ollie.servicemonitor.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * @author ollie (23.11.2021)
 */
@Accessors(chain = true)
@Data
@Generated
public class MonitorResult {

	private List<CheckResult> checkResults = new ArrayList<>();

	public MonitorResult addCheckResults(CheckResult... checkResults) {
		for (CheckResult checkResult : checkResults) {
			this.checkResults.add(checkResult);
		}
		return this;
	}

}