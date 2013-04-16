package br.ufpe.cin.groundhog.codehistory;

import br.ufpe.cin.groundhog.GroundhogException;

public class UnsupportedSCMException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public UnsupportedSCMException(String msg) {
		super(msg);
	}
}