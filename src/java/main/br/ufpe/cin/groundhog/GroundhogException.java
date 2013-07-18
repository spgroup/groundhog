package br.ufpe.cin.groundhog;

/**
 * The base {@link Exception} type for Groundhog. All Groundhog exceptions must
 * extend this class.
 * 
 * @author fjsj, gustavopinto, Rodrigo Alves
 * @since 0.0.1
 */
public class GroundhogException extends RuntimeException {
	private static final long serialVersionUID = -3563928567447310893L;

	public GroundhogException(String msg) {
		super(msg);
	}

	public GroundhogException(Throwable cause) {
		super(cause);
	}

	public GroundhogException(String msg, Throwable cause) {
		super(msg, cause);
	}
}