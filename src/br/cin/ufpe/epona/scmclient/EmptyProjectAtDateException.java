package br.cin.ufpe.epona.scmclient;

import br.cin.ufpe.epona.EponaException;

public class EmptyProjectAtDateException extends EponaException {
	
	private static final long serialVersionUID = 1L;
	
	public EmptyProjectAtDateException(String msg) {
		super(msg);
	}
	
}
