package br.ufpe.cin.groundhog.metrics;

import java.net.UnknownHostException;

import br.ufpe.cin.groundhog.database.GroundhogDB;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;

public class Teste2 {

	public static void main(String[] args) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException, InvalidTestSourcePathException, InvalidJavaFileException, UnknownHostException {
		GroundhogDB ghdb = new GroundhogDB("127.0.0.1", "java_metrics");
		ghdb.getMapper().mapPackage("br.ufpe.cin.groundhog.metrics");
		
		JavaFile j = new JavaFile("oi");
		JavaProject project = new JavaProject("src/main/resources/scribe-java/");
		project.generateStructure("src/main", "src/test");
		System.out.println("Scanning packages to project:");
		System.out.println(project.toString());
		System.out.println("################################################");
		project.generateMetrics(true,ghdb);
	}
}
