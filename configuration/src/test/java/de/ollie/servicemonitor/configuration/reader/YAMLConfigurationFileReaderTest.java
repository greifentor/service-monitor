package de.ollie.servicemonitor.configuration.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.ollie.servicemonitor.configuration.CheckConfiguration;
import de.ollie.servicemonitor.configuration.CheckConfiguration.ReturnType;
import de.ollie.servicemonitor.configuration.GroupConfiguration;
import de.ollie.servicemonitor.configuration.MonitoringConfiguration;
import de.ollie.servicemonitor.configuration.OutputColumnConfiguration.Alignment;
import de.ollie.servicemonitor.configuration.OutputConfiguration;

@ExtendWith(MockitoExtension.class)
class YAMLConfigurationFileReaderTest {

	private static final String AUTHENTICATION_BEARER = "authentication bearer";
	private static final String CHECK_EXPRESSION_0 = "checkExpression0";
	private static final String CHECK_NAME_0 = "checkName0";
	private static final String GROUP_NAME_0 = "groupName0";
	private static final String HOST_0 = "host0";
	private static final String PATH_0 = "path0";
	private static final Integer PORT_0 = 4711;
	private static final ReturnType RETURN_TYPE_0 = CheckConfiguration.ReturnType.STRING;

	private static final Alignment COLUMN_ALIGNMENT = Alignment.LEFT;
	private static final String COLUMN_CONTENT = "columnContent";
	private static final String COLUMN_ID = "columnId";
	private static final String COLUMN_NAME = "columnName";
	private static final Integer COLUMN_WIDTH = 42;
	private static final String OUTPUT_ALT_CONTENT = "outputAltContent";
	private static final String OUTPUT_ALT_ID = "outputAltIdAlt";

	private static final String CONTENT = "groups:\n" + //
			"- name: " + GROUP_NAME_0 + "\n" + //
			"  checks:\n" + //
			"  - name: " + CHECK_NAME_0 + "\n" + //
			"    host: " + HOST_0 + "\n" + //
			"    port: " + PORT_0 + "\n" + //
			"    path: " + PATH_0 + "\n" + //
			"    returnType: " + RETURN_TYPE_0.name() + "\n" + //
			"    checkExpression: " + CHECK_EXPRESSION_0 + "\n" + //
			"    outputAlternatives:\n" + //
			"    - id: " + OUTPUT_ALT_ID + "\n" + //
			"      content: " + OUTPUT_ALT_CONTENT + "\n" + //
			"  output:\n" + //
			"    columns:\n" + //
			"    - name: " + COLUMN_NAME + "\n" + //
			"      id: " + COLUMN_ID + "\n" + //
			"      content: " + COLUMN_CONTENT + "\n" + //
			"      align: " + COLUMN_ALIGNMENT.name() + "\n" + //
			"      width: " + COLUMN_WIDTH + "\n";

	private static final String CONTENT_WITH_AUTHENTICATION_BEARER = "groups:\n" + //
			"- name: " + GROUP_NAME_0 + "\n" + //
			"  checks:\n" + //
			"  - name: " + CHECK_NAME_0 + "\n" + //
			"    host: " + HOST_0 + "\n" + //
			"    port: " + PORT_0 + "\n" + //
			"    path: " + PATH_0 + "\n" + //
			"    authenticationBearer: " + AUTHENTICATION_BEARER + "\n" + //
			"    returnType: " + RETURN_TYPE_0.name() + "\n" + //
			"    checkExpression: " + CHECK_EXPRESSION_0 + "\n" + //
			"    outputAlternatives:\n" + //
			"    - id: " + OUTPUT_ALT_ID + "\n" + //
			"      content: " + OUTPUT_ALT_CONTENT + "\n" + //
			"  output:\n" + //
			"    columns:\n" + //
			"    - name: " + COLUMN_NAME + "\n" + //
			"      id: " + COLUMN_ID + "\n" + //
			"      content: " + COLUMN_CONTENT + "\n" + //
			"      align: " + COLUMN_ALIGNMENT.name() + "\n" + //
			"      width: " + COLUMN_WIDTH + "\n";

	@InjectMocks
	private YAMLConfigurationFileReader unitUnderTest;

	@Nested
	class TestsOfMethod_read_String {

		@Nested
		class ExceptionalBehavior {

			@Test
			void passANameOfANotExistingFile_throwsAnException() {
				assertThrows(FileNotFoundException.class, () -> unitUnderTest.read("/unexist/ing/file.name"));
			}

		}

		@Nested
		class ReadsDataFromTheFile {

			@Test
			void passANameOfAnExistingFile_returnsObjectWithCorrectlySetGroups(@TempDir File tempDir) throws Exception {
				// Prepare
				String fileName = tempDir.getAbsolutePath() + "/test.yml";
				Files.writeString(Path.of(fileName), CONTENT, StandardOpenOption.CREATE_NEW);
				// Run
				MonitoringConfiguration returned = unitUnderTest.read(fileName);
				// Check
				assertEquals(1, returned.getGroups().size());
				assertEquals(GROUP_NAME_0, returned.getGroups().get(0).getName());
			}

			@Test
			void passANameOfAnExistingFile_returnsObjectWithCorrectlySetChecksForGroup0(@TempDir File tempDir)
					throws Exception {
				// Prepare
				String fileName = tempDir.getAbsolutePath() + "/test.yml";
				Files.writeString(Path.of(fileName), CONTENT, StandardOpenOption.CREATE_NEW);
				// Run
				MonitoringConfiguration returned = unitUnderTest.read(fileName);
				// Check
				assertEquals(1, returned.getGroups().size());
				GroupConfiguration group = returned.getGroups().get(0);
				assertEquals(1, group.getChecks().size());
				CheckConfiguration check = group.getChecks().get(0);
				assertNull(check.getAuthenticationBearer());
				assertEquals(CHECK_EXPRESSION_0, check.getCheckExpression());
				assertEquals(CHECK_NAME_0, check.getName());
				assertEquals(HOST_0, check.getHost());
				assertEquals(PATH_0, check.getPath());
				assertEquals(PORT_0, check.getPort());
				assertEquals(RETURN_TYPE_0, check.getReturnType());
				assertNotNull(check.getOutputAlternatives());
				assertFalse(check.getOutputAlternatives().isEmpty());
				assertEquals(OUTPUT_ALT_ID, check.getOutputAlternatives().get(0).getId());
				assertEquals(OUTPUT_ALT_CONTENT, check.getOutputAlternatives().get(0).getContent());
				assertNotNull(group.getOutput());
				OutputConfiguration output = group.getOutput();
				assertFalse(output.getColumns().isEmpty());
				assertEquals(COLUMN_ALIGNMENT, output.getColumns().get(0).getAlign());
				assertEquals(COLUMN_ID, output.getColumns().get(0).getId());
				assertEquals(COLUMN_CONTENT, output.getColumns().get(0).getContent());
				assertEquals(COLUMN_NAME, output.getColumns().get(0).getName());
				assertEquals(COLUMN_WIDTH, output.getColumns().get(0).getWidth());
			}

			@Test
			void passANameOfAnExistingFileWithAuthenticationBearer_returnsObjectWithCorrectlySetChecksForGroup0(
					@TempDir File tempDir) throws Exception {
				// Prepare
				String fileName = tempDir.getAbsolutePath() + "/test.yml";
				Files.writeString(Path.of(fileName), CONTENT_WITH_AUTHENTICATION_BEARER, StandardOpenOption.CREATE_NEW);
				// Run
				MonitoringConfiguration returned = unitUnderTest.read(fileName);
				// Check
				assertEquals(1, returned.getGroups().size());
				GroupConfiguration group = returned.getGroups().get(0);
				assertEquals(1, group.getChecks().size());
				CheckConfiguration check = group.getChecks().get(0);
				assertEquals(AUTHENTICATION_BEARER, check.getAuthenticationBearer());
				assertEquals(CHECK_EXPRESSION_0, check.getCheckExpression());
				assertEquals(CHECK_NAME_0, check.getName());
				assertEquals(HOST_0, check.getHost());
				assertEquals(PATH_0, check.getPath());
				assertEquals(PORT_0, check.getPort());
				assertEquals(RETURN_TYPE_0, check.getReturnType());
				assertNotNull(group.getOutput());
				OutputConfiguration output = group.getOutput();
				assertFalse(output.getColumns().isEmpty());
				assertEquals(COLUMN_ALIGNMENT, output.getColumns().get(0).getAlign());
				assertEquals(COLUMN_ID, output.getColumns().get(0).getId());
				assertEquals(COLUMN_CONTENT, output.getColumns().get(0).getContent());
				assertEquals(COLUMN_NAME, output.getColumns().get(0).getName());
				assertEquals(COLUMN_WIDTH, output.getColumns().get(0).getWidth());
			}

		}

	}

	void showStructure() throws Exception {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		MonitoringConfiguration configuration = new MonitoringConfiguration()
				.setGroups(List.of(new GroupConfiguration().setName("groupName")
						.setChecks(List.of(new CheckConfiguration().setName("checkName")))));
		System.out.println(mapper.writeValueAsString(configuration));
	}

}
