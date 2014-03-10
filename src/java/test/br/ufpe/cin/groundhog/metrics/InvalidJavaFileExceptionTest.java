package br.ufpe.cin.groundhog.metrics;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Test;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;

public class InvalidJavaFileExceptionTest {

	@Test
	public void testInvalidJavaFileException() {
		try {
			new JavaFile("invalid/path/");
			fail("Should have thrown an InvalidJavaFileException because the java file path is invalid!");
		} catch (InvalidJavaFileException e) {
			assertThat(e.getMessage(), containsString("Invalid Java File path\nPlease check you project structure"));
		}
	}

}
