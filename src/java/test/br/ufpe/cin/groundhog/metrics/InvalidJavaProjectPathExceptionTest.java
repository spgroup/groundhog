package br.ufpe.cin.groundhog.metrics;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Test;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;

public class InvalidJavaProjectPathExceptionTest {

	@Test
	public void testInvalidJavaProjectPathException() {
		try {
			new JavaProject("/home/tulio/projetos/github/scribe-java/");
			fail("Should have thrown an InvalidJavaProjectPathException because the project path is invalid!");
		}catch(InvalidJavaProjectPathException e) {
			assertThat(e.getMessage(), containsString("Invalid Java project path"));
		}
	}
}