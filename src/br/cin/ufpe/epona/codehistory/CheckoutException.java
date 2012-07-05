package br.cin.ufpe.epona.codehistory;

public class CheckoutException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CheckoutException(Exception e) {
		super(e);
	}
	
}
