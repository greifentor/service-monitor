package de.ollie.servicemonitor;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.MonitorResult;

/**
 * @author ollie (23.11.2021)
 */
@Named
public class MonitorService {

	@Inject
	private CheckService checkService;

	public MonitorResult monitor(List<CheckRequest> checkRequests) {
		ensure(checkRequests != null, "list of check requests cannot be null.");
		MonitorResult result = createMonitorResult();
		checkRequests.forEach(checkRequest -> result.addCheckResults(checkService.performCheck(checkRequest)));
		return result;
	}

	private MonitorResult createMonitorResult() {
		return new MonitorResult();
	}

}
