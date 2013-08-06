package br.ufpe.cin.groundhog.parser;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 *  Raise an exception when no source code is found during parsing
 *  
 * @author ghlp
 * @since 0.1.0
 */
public class NotAProjectException extends GroundhogException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2115582632203381099L;

	public NotAProjectException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public NotAProjectException() {
		super();
	}

	public NotAProjectException(String msg) {
		super(msg);
	}

	public NotAProjectException(Throwable cause) {
		super(cause);
	}
}
