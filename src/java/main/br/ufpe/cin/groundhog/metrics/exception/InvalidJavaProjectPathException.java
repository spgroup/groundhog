package br.ufpe.cin.groundhog.metrics.exception;

import br.ufpe.cin.groundhog.GroundhogException;

public class InvalidJavaProjectPathException extends GroundhogException {

	/**
	 * If an invalid Java path has passed this exception will be raised
	 * @author Bruno Soares
	 * @since 0.1.0
	 */
	
	private static final long serialVersionUID = 7505823601337916084L;
	private static final String message = "Invalid Java project path";
	
	public InvalidJavaProjectPathException() { super(InvalidJavaProjectPathException.message); }
	public InvalidJavaProjectPathException(String message, Throwable cause) { super(InvalidJavaProjectPathException.message, cause); }
}
