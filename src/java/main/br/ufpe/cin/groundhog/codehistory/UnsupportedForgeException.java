package br.ufpe.cin.groundhog.codehistory;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * Thrown when an attempt to use an unsupported SCM is done
 * 
 * @author fjsj
 */
public class UnsupportedForgeException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public UnsupportedForgeException(String msg) {
		super(msg);
	}
}