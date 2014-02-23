package br.ufpe.cin.groundhog.metrics.exception;

public class InvalidSourceRootCodePathException extends Exception {

	/**
	 * If an invalid Java path has passed this exception will be raised
	 * @author Bruno Soares
	 * @since 0.1.0
	 */
	
	private static final long serialVersionUID = 476505897031339694L;
	private static final String message = "Invalid source root code path";
	
	public InvalidSourceRootCodePathException() { super(InvalidSourceRootCodePathException.message); }
	public InvalidSourceRootCodePathException(String message, Throwable cause) { super(InvalidSourceRootCodePathException.message, cause); }
	
}
