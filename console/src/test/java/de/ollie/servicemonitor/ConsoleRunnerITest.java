package de.ollie.servicemonitor;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConsoleRunnerITest {

	@Inject
	private ConsoleRunnerApplication unitUnderTest;

	@Test
	void happyRun() {
		// Prepare
		// Run
		unitUnderTest.main(new String[0]);
		// Check
	}

}