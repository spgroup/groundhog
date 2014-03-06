package br.ufpe.cin.groundhog.metrics;

import static org.junit.Assert.*;

import org.junit.Test;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;

public class GenerateMetricsWithoutPersistenceTest {

	@Test
	public void test() {
		try {
			JavaProject project = new JavaProject("src/main/resources/scribe-java/");
			project.generateStructure("src/main", "src/test");
			project.generateMetrics(false);
			long linesOfCode = 0;
			for (JavaPackage table : project.getCode_packages()){
				linesOfCode += table.getTable().TLOC_sum;
			}
			assertEquals("There should be 2808 lines of code in the source mais code", 2808, linesOfCode);
		} catch (InvalidJavaProjectPathException | InvalidSourceRootCodePathException | InvalidTestSourcePathException | InvalidJavaFileException e) {
			e.printStackTrace();
		}
	}

}
