package br.ufpe.cin.groundhog.parser.java.formater;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * 
 * @author ghlp
 * @since 0.0.1
 */
public class FormaterFactory {

	/**
	 * Factory method to create a {@link Formater} object
	 * @param formater
	 * @return
	 */
	public static Formater get(Class<? extends Formater> formater) {
		try {
			return formater.newInstance();
		} catch (Exception e) {
			String msg = String.format("I did not reconginze this output format (%s) :( I can only format in CSV or JSON", formater.getSimpleName());
			throw new GroundhogException(msg, e);
		}
	}
}
