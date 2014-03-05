package br.ufpe.cin.groundhog.metrics;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;

public class Teste2 {

	public static void main(String[] args) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException, InvalidTestSourcePathException, InvalidJavaFileException {
		JavaProject project = new JavaProject("/home/bruno/scm/github.com/groundhog2");
		project.generateStructure("src/java/main", "src/java/test");
		System.out.println("Scanning packages to project:");
		System.out.println(project.toString());
		System.out.println("################################################");
		project.generateMetrics(true);
	}
}
