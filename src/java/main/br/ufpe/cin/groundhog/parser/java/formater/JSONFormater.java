package br.ufpe.cin.groundhog.parser.java.formater;

import java.util.HashMap;

import br.ufpe.cin.groundhog.parser.java.MutableInt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONFormater extends Formater {

	/**
	 * Creates an object that represents the JSON result of the metrics.
	 * @return JSONObject that embodies the result
	 */
	@Override
	public String format(HashMap<String, HashMap<String, MutableInt>> object) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(object);
	}
}
