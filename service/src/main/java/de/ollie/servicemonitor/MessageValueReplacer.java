package de.ollie.servicemonitor;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.Map;
import java.util.StringTokenizer;

import javax.inject.Named;

import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckResult;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor
public class MessageValueReplacer {

	private final ValueMapPathExtractor valueMapPathExtractor;

	public String getMessageWithReplacesValues(String content, CheckRequest checkRequest, CheckResult checkResult) {
		ensure(checkRequest != null, "check request cannot be null.");
		ensure(checkResult != null, "check result cannot be null.");
		if (content != null) {
			content = content.replace("${url}", checkRequest.getUrl());
			content = content.replace("${name}", checkRequest.getName());
			content = content.replace("${status}", checkResult.getStatus().name());
			content = replaceValueMapEntries(content, checkResult.getValueMap());
		}
		return content;
	}

	private String replaceValueMapEntries(String content, Map<String, Object> valueMap) {
		while (content.contains("$F{")) {
			String path = valueMapPathExtractor.extractPath(content).orElse(null);
			Object o = ((path != null) ? getValueMapEntry(path, valueMap) : null);
			content = content.replace("$F{" + path + "}", String.valueOf(o));
		}
		return content;
	}

	private Object getValueMapEntry(String path, Map<String, Object> valueMap) {
		if (valueMap == null) {
			return null;
		}
		StringTokenizer pathElements = new StringTokenizer(path, ".");
		Object o = null;
		while (pathElements.hasMoreTokens()) {
			String element = pathElements.nextToken();
			o = valueMap.get(element);
			if (o == null) {
				return null;
			}
			if (o instanceof Map) {
				valueMap = (Map) o;
			}
		}
		return o;
	}

}
