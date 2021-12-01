package de.ollie.servicemonitor;

import static de.ollie.servicemonitor.util.Check.ensure;

import javax.inject.Named;

import de.ollie.servicemonitor.model.CheckRequest;

@Named
public class MessageValueReplacer {

	public String getMessageWithReplacesValues(CheckRequest checkRequest) {
		ensure(checkRequest != null, "check request cannot be null.");
		String message = checkRequest.getMessage();
		if (message != null) {
			message = message.replace("${url}", checkRequest.getUrl());
		}
		return message;
	}

}
