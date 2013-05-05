package br.ufpe.cin.groundhog.scmclient;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * Thrown when a checkout operation is attempted for a date that corresponds
 * to phase when the project has zero commits.
 * @author fjsj
 *
 */
public class EmptyProjectAtDateException extends GroundhogException {	
	private static final long serialVersionUID = 1L;
	
	public EmptyProjectAtDateException(String msg) {
		super(msg);
	}
}