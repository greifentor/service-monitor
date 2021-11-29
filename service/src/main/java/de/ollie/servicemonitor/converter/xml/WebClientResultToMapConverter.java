package de.ollie.servicemonitor.converter.xml;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import de.ollie.servicemonitor.model.CheckRequest.ReturnedMediaType;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor
public class WebClientResultToMapConverter {

	public Map<String, Object> convert(String s, ReturnedMediaType returnedMediaType) {
		ensure(returnedMediaType != null, "returned media type cannot be null.");
		if (s == null) {
			return null;
		}
		switch (returnedMediaType) {
		case STRING:
			return new HashMap<>(Map.of("STRING", s));
		default:
			break;
		}
		return null;
	}

}
