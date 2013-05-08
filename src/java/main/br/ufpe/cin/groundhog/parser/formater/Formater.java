package br.ufpe.cin.groundhog.parser.formater;

import java.util.HashMap;

import br.ufpe.cin.groundhog.parser.MutableInt;

public interface Formater {

	String format(HashMap<String, HashMap<String, MutableInt>> object);

}
