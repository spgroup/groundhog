package br.cin.ufpe.groundhog.scmclient;

import br.cin.ufpe.groundhog.GroundhogException;

public class EmptyProjectAtDateException extends GroundhogException {
	
	private static final long serialVersionUID = 1L;
	
	public EmptyProjectAtDateException(String msg) {
		super(msg);
	}
	
}
