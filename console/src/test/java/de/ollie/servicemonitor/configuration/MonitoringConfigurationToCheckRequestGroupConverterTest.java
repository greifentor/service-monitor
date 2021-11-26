package de.ollie.servicemonitor.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.model.CheckRequestGroup;

@ExtendWith(MockitoExtension.class)
public class MonitoringConfigurationToCheckRequestGroupConverterTest {

	private static final String CHECK_EXPRESSION_0 = "check expression 0";
	private static final String CHECK_EXPRESSION_1 = "check expression 1";
	private static final String CHECK_NAME_0 = "check name 0";
	private static final String CHECK_NAME_1 = "check name 1";
	private static final String GROUP_NAME_0 = "group name 0";
	private static final String GROUP_NAME_1 = "group name 1";
	private static final String URL_0 = "url 0";
	private static final String URL_1 = "url 1";

	@InjectMocks
	private MonitoringConfigurationToCheckRequestGroupConverter unitUnderTest;

	@Nested
	class TestsOfMethod_convert_MonitoringConfiguration {

		@Test
		void passANullValue_returnsANullValue() {
			assertNull(unitUnderTest.convert(null));
		}

		@Test
		void passAMonitorConfigurationWithNoGroupConfigurations_returnsAnEmptyList() {
			// Prepare
			MonitoringConfiguration monitoringConfiguration = new MonitoringConfiguration();
			// Run
			List<CheckRequestGroup> returned = unitUnderTest.convert(monitoringConfiguration);
			// Check
			assertTrue(returned.isEmpty());
		}

		@Test
		void passAMonitorConfigurationWithOneEmptyGroupConfigurationsWithOnlyAName_returnsAListWithACorrectCheckRequestGroup() {
			// Prepare
			MonitoringConfiguration monitoringConfiguration = new MonitoringConfiguration()
					.setGroups(List.of(new GroupConfiguration().setName(GROUP_NAME_0)));
			// Run
			List<CheckRequestGroup> returned = unitUnderTest.convert(monitoringConfiguration);
			// Check
			assertEquals(1, returned.size());
			assertEquals(GROUP_NAME_0, returned.get(0).getName());
			assertTrue(returned.get(0).getCheckRequests().isEmpty());
		}

		@Test
		void passAMonitorConfigurationWithTwoEmptyGroupConfigurationsWithOnlyAName_returnsAListWithTheCorrectCheckRequestGroups() {
			// Prepare
			MonitoringConfiguration monitoringConfiguration = new MonitoringConfiguration()
					.setGroups(List.of(new GroupConfiguration().setName(GROUP_NAME_0),
							new GroupConfiguration().setName(GROUP_NAME_1)));
			// Run
			List<CheckRequestGroup> returned = unitUnderTest.convert(monitoringConfiguration);
			// Check
			assertEquals(2, returned.size());
			assertEquals(GROUP_NAME_0, returned.get(0).getName());
			assertTrue(returned.get(0).getCheckRequests().isEmpty());
			assertEquals(GROUP_NAME_1, returned.get(1).getName());
			assertTrue(returned.get(1).getCheckRequests().isEmpty());
		}

		@Test
		void passAMonitorConfigurationWithAFullyLoadedGroupConfigurations_returnsAListWithACorrectCheckRequestGroup() {
			// Prepare
			MonitoringConfiguration monitoringConfiguration = new MonitoringConfiguration()
					.setGroups(List.of(new GroupConfiguration()
							.setChecks(List.of(new CheckConfiguration().setCheckExpression(CHECK_EXPRESSION_0)
									.setName(CHECK_NAME_0)
									.setUrl(URL_0)))
							.setName(GROUP_NAME_0)));
			// Run
			List<CheckRequestGroup> returned = unitUnderTest.convert(monitoringConfiguration);
			// Check
			CheckRequestGroup group = returned.get(0);
			assertTrue(group.getCheckRequests().isEmpty());
		}

	}

}
