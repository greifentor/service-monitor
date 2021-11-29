package de.ollie.servicemonitor;

import java.util.Map;

import javax.inject.Named;

import de.ollie.servicemonitor.converter.xml.WebClientResultToMapConverter;
import de.ollie.servicemonitor.evaluation.CheckExpressionEvaluator;
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

	private final CheckExpressionEvaluator checkExpressionEvaluator;
	private final WebClientResultToMapConverter webClientResultToMapConverter;

	/**
	 * Checks all passed check request and returns a check result.
	 * 
	 * @param checkRequest A request for the checks which are to perform.
	 * @return A check result for the passed request.
	 */
	public CheckResult performCheck(CheckRequest checkRequest) {
		try {
			String callResult = webClient.call(checkRequest.getUrl());
			Map<String, Object> valueMap = webClientResultToMapConverter.convert(callResult,
					checkRequest.getReturnedMediaType());
			Object result = checkExpressionEvaluator.evaluate(checkRequest.getCheckExpression(), valueMap);
			return new CheckResult()
					.setStatus((result instanceof Boolean) && (Boolean) result ? Status.OK : Status.FAIL);
		} catch (Exception e) {
			e.printStackTrace();
			return new CheckResult().setStatus(Status.ERROR);
		}
	}

}
