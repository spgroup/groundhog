package br.ufpe.cin.groundhog.metrics;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Test;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;

public class InvalidTestSourcePathExceptionTest {

	@Test
	public void testInvalidTestSourcePathException() {
		try {
			JavaProject jp = new JavaProject("src/main/resources/scribe-java/");
			jp.generateStructure("src/main/", "invalid/path/");
			fail("Should have thrown an InvalidTestSourcePathException because the test path is invalid!");
		}catch(InvalidTestSourcePathException e) {
			assertThat(e.getMessage(), containsString("Invalid test source root code"));
		} catch (InvalidJavaProjectPathException e) {
			fail("Should have thrown an InvalidTestSourcePathException, but another exception was thrown!");
		} catch (InvalidSourceRootCodePathException e) {
			fail("Should have thrown an InvalidTestSourcePathException, but another exception was thrown!");
		} catch (InvalidJavaFileException e) {
			fail("Should have thrown an InvalidTestSourcePathException, but another exception was thrown!");
		}
	}
}