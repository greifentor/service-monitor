package de.ollie.servicemonitor.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.servicemonitor.configuration.CheckConfiguration.ReturnType;
import de.ollie.servicemonitor.configuration.OutputColumnConfiguration.Alignment;
import de.ollie.servicemonitor.model.CheckRequest;
import de.ollie.servicemonitor.model.CheckRequest.ReturnedMediaType;
import de.ollie.servicemonitor.model.CheckRequestGroup;

@ExtendWith(MockitoExtension.class)
class MonitoringConfigurationToCheckRequestGroupConverterTest {

	private static final String AUTHENTICATION_BEARER = "authentication bearer";
	private static final String CHECK_EXPRESSION_0 = "check expression 0";
	private static final String CHECK_EXPRESSION_1 = "check expression 1";
	private static final String CHECK_NAME_0 = "check name 0";
	private static final String CHECK_NAME_1 = "check name 1";
	private static final String GROUP_NAME_0 = "group name 0";
	private static final String GROUP_NAME_1 = "group name 1";
	private static final String HOST_0 = "host0";
	private static final String HOST_1 = "host1";
	private static final String OUTPUT_ALT_CONTENT = "outputAltContent";
	private static final String OUTPUT_ALT_ID = "outputAltId";
	private static final String PATH_0 = "path0";
	private static final String PATH_1 = "path1";
	private static final Integer PORT_0 = 4711;
	private static final Integer PORT_1 = 1701;
	private static final ReturnType RETURN_TYPE_0 = ReturnType.STRING;
	private static final ReturnType RETURN_TYPE_1 = ReturnType.JSON;

	private static final Alignment COLUMN_ALIGNMENT = Alignment.LEFT;
	private static final String COLUMN_CONTENT = "columnContent";
	private static final String COLUMN_ID = "columnId";
	private static final String COLUMN_NAME = "columnName";
	private static final Integer COLUMN_WIDTH = 42;

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
									.setHost(HOST_0)
									.setPath(PATH_0)
									.setPort(PORT_0)
									.setAuthenticationBearer(AUTHENTICATION_BEARER)
									.setOutputAlternatives(List
											.of(new OutputAlternativesConfiguration().setContent(OUTPUT_ALT_CONTENT)
													.setId(OUTPUT_ALT_ID)))))
							.setName(GROUP_NAME_0)
							.setOutput(new OutputConfiguration()
									.setColumns(List.of(new OutputColumnConfiguration().setAlign(COLUMN_ALIGNMENT)
											.setContent(COLUMN_CONTENT)
											.setId(COLUMN_ID)
											.setName(COLUMN_NAME)
											.setWidth(COLUMN_WIDTH))))));
			// Run
			List<CheckRequestGroup> returned = unitUnderTest.convert(monitoringConfiguration);
			// Check
			CheckRequestGroup group = returned.get(0);
			assertFalse(group.getCheckRequests().isEmpty());
			CheckRequest checkRequest = group.getCheckRequests().get(0);
			assertEquals(AUTHENTICATION_BEARER, checkRequest.getAuthenticationBearer());
			assertEquals(CHECK_EXPRESSION_0, checkRequest.getCheckExpression());
			assertEquals(CHECK_NAME_0, checkRequest.getName());
			assertSame(group, checkRequest.getGroup());
			assertEquals(HOST_0, checkRequest.getHost());
			assertEquals(PATH_0, checkRequest.getPath());
			assertEquals(PORT_0, checkRequest.getPort());
			assertNotNull(group.getOutput());
			assertFalse(group.getOutput().getColumns().isEmpty());
			assertEquals(COLUMN_ALIGNMENT.name(), group.getOutput().getColumns().get(0).getAlign().name());
			assertEquals(COLUMN_CONTENT, group.getOutput().getColumns().get(0).getContent());
			assertEquals(COLUMN_ID, group.getOutput().getColumns().get(0).getId());
			assertEquals(COLUMN_NAME, group.getOutput().getColumns().get(0).getName());
			assertEquals(COLUMN_WIDTH, group.getOutput().getColumns().get(0).getWidth());
		}

		@Test
		void passAMonitorConfigurationWithACheckConfigurationsHavingANullValueAsHost_throwsAnException() {
			// Prepare
			MonitoringConfiguration monitoringConfiguration =
					new MonitoringConfiguration()
							.setGroups(
									List
											.of(
													new GroupConfiguration()
															.setChecks(
																	List
																			.of(
																					new CheckConfiguration()
																							.setCheckExpression(
																									CHECK_EXPRESSION_0)
																							.setName(CHECK_NAME_0)
																							.setPath(PATH_0)
																							.setPort(PORT_0)))
															.setName(GROUP_NAME_0)));
			// Run & Check
			assertThrows(IllegalStateException.class, () -> unitUnderTest.convert(monitoringConfiguration));
		}

		@Test
		void passAMonitorConfigurationWithAFullyLoadedGroupConfigurationsWithTwoChecks_returnsAListWithACorrectCheckRequestGroup() {
			// Prepare
			MonitoringConfiguration monitoringConfiguration =
					new MonitoringConfiguration()
							.setGroups(
									List
											.of(
													new GroupConfiguration()
															.setChecks(
																	List
																			.of(
																					new CheckConfiguration()
																							.setCheckExpression(
																									CHECK_EXPRESSION_0)
																							.setName(CHECK_NAME_0)
																							.setReturnType(
																									RETURN_TYPE_0)
																							.setHost(HOST_0)
																							.setPath(PATH_0)
																							.setPort(PORT_0),
																					new CheckConfiguration()
																							.setCheckExpression(
																									CHECK_EXPRESSION_1)
																							.setName(CHECK_NAME_1)
																							.setReturnType(
																									RETURN_TYPE_1)
																							.setHost(HOST_1)
																							.setPath(PATH_1)
																							.setPort(PORT_1)))
															.setName(GROUP_NAME_0)));
			// Run
			List<CheckRequestGroup> returned = unitUnderTest.convert(monitoringConfiguration);
			// Check
			CheckRequestGroup group = returned.get(0);
			assertFalse(group.getCheckRequests().isEmpty());
			CheckRequest checkRequest = group.getCheckRequests().get(0);
			assertEquals(CHECK_EXPRESSION_0, checkRequest.getCheckExpression());
			assertEquals(CHECK_NAME_0, checkRequest.getName());
			assertEquals(ReturnedMediaType.STRING, checkRequest.getReturnedMediaType());
			assertEquals(HOST_0, checkRequest.getHost());
			assertFalse(checkRequest.isHttps());
			assertEquals(PATH_0, checkRequest.getPath());
			assertEquals(PORT_0, checkRequest.getPort());
			checkRequest = group.getCheckRequests().get(1);
			assertEquals(CHECK_EXPRESSION_1, checkRequest.getCheckExpression());
			assertEquals(CHECK_NAME_1, checkRequest.getName());
			assertEquals(ReturnedMediaType.JSON, checkRequest.getReturnedMediaType());
			assertEquals(HOST_1, checkRequest.getHost());
			assertFalse(checkRequest.isHttps());
			assertEquals(PATH_1, checkRequest.getPath());
			assertEquals(PORT_1, checkRequest.getPort());
		}

		@Test
		void passAMonitorConfigurationWithAFullyLoadedGroupConfigurationsWithAnHttpsCheck_returnsAListWithACorrectCheckRequestGroup() {
			// Prepare
			MonitoringConfiguration monitoringConfiguration =
					new MonitoringConfiguration()
							.setGroups(
									List
											.of(
													new GroupConfiguration()
															.setChecks(
																	List
																			.of(
																					new CheckConfiguration()
																							.setCheckExpression(
																									CHECK_EXPRESSION_0)
																							.setName(CHECK_NAME_0)
																							.setReturnType(
																									RETURN_TYPE_0)
																							.setHost(HOST_0)
																							.setHttps(true)
																							.setPath(PATH_0)
																							.setPort(PORT_0)))
															.setName(GROUP_NAME_0)));
			// Run
			List<CheckRequestGroup> returned = unitUnderTest.convert(monitoringConfiguration);
			// Check
			assertTrue(returned.get(0).getCheckRequests().get(0).isHttps());
		}

	}

}
