package br.ufpe.cin.groundhog.search;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * Thrown when something nasty happens
 * 
 * @author gustavopinto
 */
public class SearchException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public SearchException(Throwable e) {
		super(e);
	}
}