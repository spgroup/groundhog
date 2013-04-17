package br.ufpe.cin.groundhog.codehistory;

import br.ufpe.cin.groundhog.GroundhogException;

public class CheckoutException extends GroundhogException {
	private static final long serialVersionUID = 1L;

	public CheckoutException(Throwable e) {
		super(e);
	}

}