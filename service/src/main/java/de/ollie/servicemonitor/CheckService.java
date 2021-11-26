package de.ollie.servicemonitor;

import javax.inject.Named;

import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckResult;
import de.ollie.servicemonitor.model.CheckResult.Status;
import de.ollie.servicemonitor.web.WebClient;
import lombok.RequiredArgsConstructor;

/**
 * @author ollie (23.11.2021)
 */
@Named
@RequiredArgsConstructor
public class CheckService {
	
	private final WebClient webClient;

	/**
	 * Checks all passed check request and returns a check result.
	 * 
	 * @param checkRequest A request for the checks which are to perform.
	 * @return A check result for the passed request.
	 */
	public CheckResult performCheck(CheckRequest checkRequest) {
		return new CheckResult().setStatus(Status.OK);
	}

}
