package de.ollie.servicemonitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckResult;
import de.ollie.servicemonitor.model.MonitorResult;

@ExtendWith(MockitoExtension.class)
class MonitorServiceTest {

	@Mock
	private CheckService checkService;

	@InjectMocks
	private MonitorService unitUnderTest;

	@Nested
	class TestsOfMethod_monitor_ListCheckRequest {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANullValueAsCheckRequestList_throwsAnException() {
				assertThrows(IllegalArgumentException.class, () -> unitUnderTest.monitor(null));
			}

		}

		@Nested
		class PerformsChecks {

			@Test
			void passAListOfCheckRequests_performsEachCheck() {
				// Prepare
				CheckRequest checkRequest0 = mock(CheckRequest.class);
				CheckRequest checkRequest1 = mock(CheckRequest.class);
				List<CheckRequest> passed = List.of(checkRequest0, checkRequest1);
				CheckResult checkResult0 = mock(CheckResult.class);
				CheckResult checkResult1 = mock(CheckResult.class);
				when(checkService.performCheck(checkRequest0)).thenReturn(checkResult0);
				when(checkService.performCheck(checkRequest1)).thenReturn(checkResult1);
				// Run
				MonitorResult returned = unitUnderTest.monitor(passed);
				// Check
				assertEquals(2, returned.getCheckResults().size());
				assertTrue(returned.getCheckResults().contains(checkResult0));
				assertTrue(returned.getCheckResults().contains(checkResult1));
			}

		}

	}

}
