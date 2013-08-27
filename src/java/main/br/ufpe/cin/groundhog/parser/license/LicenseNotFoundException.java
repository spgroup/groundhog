package br.ufpe.cin.groundhog.parser.license;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * This class raises an exception when no license is found
 *  
 * @author ghlp
 * @since 0.1.0
 */
public class LicenseNotFoundException extends GroundhogException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6933755608744941883L;

	public LicenseNotFoundException(String msg) {
		super(msg);
	}

}
