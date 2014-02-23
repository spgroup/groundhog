package br.ufpe.cin.groundhog.metrics.exception;

public class InvalidTestSourcePathException extends Exception{
	
	/**
	 * If an invalid Java path has passed this exception will be raised
	 * @author Bruno Soares
	 * @since 0.1.0
	 */
	
	private static final long serialVersionUID = 2361880567057134357L;
	private static final String message = "Invalid test source root code";
	
	public InvalidTestSourcePathException() { super(InvalidTestSourcePathException.message); }
	public InvalidTestSourcePathException(String message, Throwable cause) { super(InvalidTestSourcePathException.message, cause); }
}
