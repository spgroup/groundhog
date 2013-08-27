package br.ufpe.cin.groundhog.parser.java.formater;

import java.util.HashMap;

import br.ufpe.cin.groundhog.parser.java.MutableInt;

/**
 * This class formats the object data gathered from {@link JavaParser} into file
 * formats. We currently support CSV and JSON files. 
 * 
 * @author ghlp
 * @since 0.0.1
 */
public abstract class Formater {

	/**
	 * Formats the metric object into a file representation. It could be
	 * CSV or JSON
	 * 
	 * @param object
	 * @return
	 */
	public abstract String format(HashMap<String, HashMap<String, MutableInt>> object);

	public String toString() {
		return getClass().getSimpleName().toLowerCase().replaceAll("formater", "");
	}
}
