package br.cin.ufpe.epona.codehistory;

import br.cin.ufpe.epona.EponaException;

public class CheckoutException extends EponaException {

	private static final long serialVersionUID = 1L;

	public CheckoutException(Throwable e) {
		super(e);
	}

}
