package br.ufpe.cin.groundhog.codehistory;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * Thrown when a checkout cannot be performed
 * @author fjsj, gustavopinto
 */
public class CheckoutException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public CheckoutException(String msg) {
		super(msg);
	}
	
	public CheckoutException(Throwable e) {
		super(e);
	}
}