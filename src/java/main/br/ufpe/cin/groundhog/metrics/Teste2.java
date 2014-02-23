package br.ufpe.cin.groundhog.metrics;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;

public class Teste2 {

	public static void main(String[] args) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException, InvalidTestSourcePathException {
		JavaProject project = new JavaProject("C:\\Users\\Bruno Soares\\Documents\\scm\\atunes-code\\aTunes");
		project.generateStructure("src\\main\\java", "src\\test\\java");
		System.out.println("Scanning packages to project:");
		System.out.println(project.toString());
	}
}
