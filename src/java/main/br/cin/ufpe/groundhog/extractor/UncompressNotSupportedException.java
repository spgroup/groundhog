package br.cin.ufpe.groundhog.extractor;

import br.cin.ufpe.groundhog.GroundhogException;

public class UncompressNotSupportedException extends GroundhogException {
	private static final long serialVersionUID = -5221460516926877262L;

	public UncompressNotSupportedException(String msg) {
		super(msg);
	}
	
}