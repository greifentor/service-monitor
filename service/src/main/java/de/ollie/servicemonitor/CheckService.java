package de.ollie.servicemonitor;

import static de.ollie.servicemonitor.util.Check.ensure;

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

	private final CheckExpressionEvaluator checkExpressionEvaluator;
	private final WebClient webClient;
	private final WebClientResultToMapConverter webClientResultToMapConverter;

	/**
	 * Checks all passed check request and returns a check result.
	 * 
	 * @param checkRequest A request for the checks which are to perform.
	 * @return A check result for the passed request.
	 */
	public CheckResult performCheck(CheckRequest checkRequest) {
		ensure(checkRequest != null, "check request cannot be null.");
		CheckResult checkResult = new CheckResult().setCheckRequest(checkRequest);
		try {
			WebClient.Response response = webClient.call(checkRequest.getUrl(), checkRequest.getAuthenticationBearer());
			if (!response.isOk()) {
				return checkResult.setStatus(Status.FAIL);
			}
			Map<String, Object> valueMap = webClientResultToMapConverter.convert(response.getBody(),
					checkRequest.getReturnedMediaType());
			Object result = checkExpressionEvaluator.evaluate(checkRequest.getCheckExpression(), valueMap);
			return checkResult
					.setName(checkRequest.getName())
					.setStatus((result instanceof Boolean) && (Boolean) result ? Status.OK : Status.FAIL)
					.setValueMap(valueMap);
		} catch (Exception e) {
			return checkResult
					.setErrorMessage(e.getMessage())
					.setStatus(Status.ERROR);
		}
	}

}
