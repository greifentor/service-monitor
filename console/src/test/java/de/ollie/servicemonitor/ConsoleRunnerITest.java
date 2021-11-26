package de.ollie.servicemonitor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConsoleRunnerITest {

	private static final String CONFIGURATION_FILE_NAME = "test-configuration.yml";

	@Inject
	private ConsoleRunnerApplication unitUnderTest;

	@Test
	void happyRun(@TempDir Path tempDir) throws Exception {
		// Prepare
		String configurationFileName = tempDir.toString() + "/" + CONFIGURATION_FILE_NAME;
		Files.writeString(Path.of(configurationFileName),
				"groups:\n" //
						+ "- name: TST\n" //
						+ "  checks:\n" //
						+ "  - name: Test Check\n" //
						+ "    url: localhorst:9099\n" //
						+ "    checkExpression: $status ok ==\n",
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		// Run
		unitUnderTest.main(new String[] { "--file=" + configurationFileName });
		// Check
	}

}