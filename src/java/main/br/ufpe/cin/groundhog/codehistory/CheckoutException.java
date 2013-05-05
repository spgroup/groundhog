package br.ufpe.cin.groundhog.codehistory;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * Thrown when a check out cannot be performed
 * @author fjsj
 */
public class CheckoutException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public CheckoutException(Throwable e) {
		super(e);
	}
}