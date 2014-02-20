package br.ufpe.cin.groundhog.extractor;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * Exception thrown when an extraction operation is attempted to be performed on an unsupported format 
 * @author fjsj, gustavopinto, Rodrigo Alves
 * @deprecated
 * 
 */
public class UncompressNotSupportedException extends GroundhogException {
	private static final long serialVersionUID = -5221460516926877262L;

	public UncompressNotSupportedException(String msg) {
		super(msg);
	}
}