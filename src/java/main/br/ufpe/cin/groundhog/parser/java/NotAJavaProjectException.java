package br.ufpe.cin.groundhog.parser.java;

import br.ufpe.cin.groundhog.GroundhogException;

public class NotAJavaProjectException extends GroundhogException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5966602098279205546L;

	public NotAJavaProjectException() {
		super("Project %s has no Java source files! Metrics couldn't be extracted!");
	}

	public NotAJavaProjectException(Throwable cause) {
		super(cause);
	}
}
