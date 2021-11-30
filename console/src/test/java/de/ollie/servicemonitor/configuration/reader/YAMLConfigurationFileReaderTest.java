package de.ollie.servicemonitor.configuration.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(MockitoExtension.class)
class YAMLConfigurationFileReaderTest {

	private static final String CHECK_EXPRESSION_0 = "checkExpression0";
	private static final String CHECK_NAME_0 = "checkName0";
	private static final String GROUP_NAME_0 = "groupName0";
	private static final ReturnType RETURN_TYPE_0 = CheckConfiguration.ReturnType.STRING;
	private static final String URL_0 = "url0";

	private static final String CONTENT = "groups:\n" + //
			"- name: " + GROUP_NAME_0 + "\n" + //
			"  checks:\n" + //
			"  - name: " + CHECK_NAME_0 + "\n" + //
			"    url: " + URL_0 + "\n" + //
			"    returnType: " + RETURN_TYPE_0.name() + "\n" + //
			"    checkExpression: " + CHECK_EXPRESSION_0 + "\n";

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
				assertEquals(CHECK_EXPRESSION_0, check.getCheckExpression());
				assertEquals(CHECK_NAME_0, check.getName());
				assertEquals(RETURN_TYPE_0, check.getReturnType());
				assertEquals(URL_0, check.getUrl());
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
