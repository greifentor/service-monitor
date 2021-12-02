package de.ollie.servicemonitor;

import static de.ollie.servicemonitor.util.Check.ensure;

import java.util.Optional;

import javax.inject.Named;

@Named
public class ValueMapPathExtractor {

	public Optional<String> extractPath(String s) {
		ensure(s != null, "s cannot be null.");
		return Optional.ofNullable(s.contains("$F{") ? s.substring(s.indexOf("$F{") + 3, s.indexOf("}")) : null);
	}

}
