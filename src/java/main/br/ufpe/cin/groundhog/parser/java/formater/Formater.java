package br.ufpe.cin.groundhog.parser.java.formater;

import java.util.HashMap;

import br.ufpe.cin.groundhog.parser.java.MutableInt;

public abstract class Formater {

	public abstract String format(HashMap<String, HashMap<String, MutableInt>> object);

	public String simpleName() {
		return getClass().getSimpleName().toLowerCase().replaceAll("formater", "");
	}
	
	public String toString() {
		return simpleName();
	}
}
