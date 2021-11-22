package de.ollie.servicemonitor.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MonitorResultTest {

	@InjectMocks
	private MonitorResult unitUnderTest;

	@Nested
	class TestsOfMethod_addCheckResults_CheckResults {

		@Test
		void passAnEmptyList_addsNothing() {
			assertTrue(unitUnderTest.addCheckResults().getCheckResults().isEmpty());
		}

		@Test
		void passAListOfCheckRequests_addsAllPassedRequests() {
			// Prepare
			CheckResult checkResult0 = mock(CheckResult.class);
			CheckResult checkResult1 = mock(CheckResult.class);
			// Run
			MonitorResult returned = unitUnderTest.addCheckResults(checkResult0, checkResult1);
			// Check
			assertEquals(2, returned.getCheckResults().size());
			assertTrue(returned.getCheckResults().contains(checkResult0));
			assertTrue(returned.getCheckResults().contains(checkResult1));
		}

	}

}