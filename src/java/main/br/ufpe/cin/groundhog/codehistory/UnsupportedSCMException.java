package br.ufpe.cin.groundhog.codehistory;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * Thrown when an attempt to use an unsupported SCM is done
 * @author fjsj
 */
public class UnsupportedSCMException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public UnsupportedSCMException(String msg) {
		super(msg);
	}
}