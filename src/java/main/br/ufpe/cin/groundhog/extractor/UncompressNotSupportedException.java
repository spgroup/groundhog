package br.ufpe.cin.groundhog.extractor;

import br.ufpe.cin.groundhog.GroundhogException;

public class UncompressNotSupportedException extends GroundhogException {
	private static final long serialVersionUID = -5221460516926877262L;

	public UncompressNotSupportedException(String msg) {
		super(msg);
	}
	
}