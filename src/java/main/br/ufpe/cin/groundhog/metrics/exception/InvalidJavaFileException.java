package br.ufpe.cin.groundhog.metrics.exception;

public class InvalidJavaFileException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7197149220798206965L;
	private static final String message = "Invalid Java File path\nPlease check you project structures";
	
	public InvalidJavaFileException() { super(InvalidJavaFileException.message); }
	public InvalidJavaFileException(String message, Throwable cause) { super(InvalidJavaFileException.message, cause); }
}
