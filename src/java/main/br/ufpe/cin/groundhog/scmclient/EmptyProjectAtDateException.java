package br.ufpe.cin.groundhog.scmclient;

import br.ufpe.cin.groundhog.GroundhogException;

public class EmptyProjectAtDateException extends GroundhogException {	
	private static final long serialVersionUID = 1L;
	
	public EmptyProjectAtDateException(String msg) {
		super(msg);
	}
	
}