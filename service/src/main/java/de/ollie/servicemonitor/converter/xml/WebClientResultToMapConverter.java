package de.ollie.servicemonitor.converter.xml;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.ollie.servicemonitor.model.CheckRequest.ReturnedMediaType;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor
public class WebClientResultToMapConverter {

	private final XMLStringToMapConverter xmlStringToMapConverter;

	private ObjectMapper mapper = new ObjectMapper();

	public Map<String, Object> convert(String s, ReturnedMediaType returnedMediaType) {
		ensure(returnedMediaType != null, "returned media type cannot be null.");
		if (s == null) {
			return null;
		}
		try {
			switch (returnedMediaType) {
			case JSON:
				return mapper.readValue(s, Map.class);
			case STRING:
				return new HashMap<>(Map.of("STRING", s));
			case XML:
				return xmlStringToMapConverter.convert(s);
			default:
				break;
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(
					"something went wrong while converting to map! s=" + s + ", returnedMediaType=" + returnedMediaType
							+ ", errorMessage=" + e.getMessage(),
					e);
		}
	}

}
