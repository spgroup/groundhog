package br.ufpe.cin.groundhog.metrics.exception;

import br.ufpe.cin.groundhog.GroundhogException;

public class InvalidJavaFileException extends GroundhogException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7197149220798206965L;
	private static final String message = "Invalid Java File path\nPlease check you project structures";
	
	public InvalidJavaFileException() { super(InvalidJavaFileException.message); }
	public InvalidJavaFileException(String message, Throwable cause) { super(InvalidJavaFileException.message, cause); }
}
