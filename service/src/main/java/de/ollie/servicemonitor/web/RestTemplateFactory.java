package de.ollie.servicemonitor.web;

import javax.inject.Named;

import org.springframework.web.client.RestTemplate;

@Named
public class RestTemplateFactory {

	public RestTemplate create() {
		return new RestTemplate();
	}

}