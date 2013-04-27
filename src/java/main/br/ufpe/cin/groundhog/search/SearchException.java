package br.ufpe.cin.groundhog.search;

import br.ufpe.cin.groundhog.GroundhogException;

public class SearchException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public SearchException(Exception e) {
		super(e);
	}
}