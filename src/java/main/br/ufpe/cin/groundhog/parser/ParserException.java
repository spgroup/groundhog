package br.ufpe.cin.groundhog.parser;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * 
 * @author ghlp
 * @since 0.1.0
 */
public class ParserException extends GroundhogException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2115582632203381099L;

	public ParserException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ParserException() {
		super();
	}

	public ParserException(String msg) {
		super(msg);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}
}
