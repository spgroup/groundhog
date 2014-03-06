package br.ufpe.cin.groundhog.metrics;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Test;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;

public class InvalidSourceRootCodePathExceptionTest {

	@Test
	public void testInvalidSourceRootCodePathException() {
		try {
			JavaProject jp = new JavaProject("src/main/resources/scribe-java-project/");
			jp.generateStructure("invalid/path/", "src/test/");
			fail("Should have thrown an InvalidSourceRootCodePathException because the source root code path is invalid!");
		}catch(InvalidSourceRootCodePathException e) {
			assertThat(e.getMessage(), containsString("Invalid source root code path"));
		} catch (InvalidJavaProjectPathException e) {
			fail("Should have thrown an InvalidSourceRootCodePathException, but another exception was thrown!");
		} catch (InvalidTestSourcePathException e) {
			fail("Should have thrown an InvalidSourceRootCodePathException, but another exception was thrown!");
		} catch (InvalidJavaFileException e) {
			fail("Should have thrown an InvalidSourceRootCodePathException, but another exception was thrown!");
		}
	}
}