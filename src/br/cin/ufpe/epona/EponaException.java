package br.cin.ufpe.epona;

/**
 * The base {@link Exception} type for Epona. All Epona exceptions must be extends this.
 */

public class EponaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3563928567447310893L;

	public EponaException(String msg) {
		super(msg);
	}
	
	public EponaException(Throwable cause) {
		super(cause);
	}
	
	public EponaException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
