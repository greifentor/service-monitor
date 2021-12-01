package de.ollie.servicemonitor.web;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class RestTemplateFactoryTest {

	@InjectMocks
	private RestTemplateFactory unitUnderTest;

	@Test
	void createMethodReturnsAnInstanceOfRestTemplate() {
		assertTrue(unitUnderTest.create() instanceof RestTemplate);
	}

}
