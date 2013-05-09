package br.ufpe.cin.groundhog.parser.formater;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufpe.cin.groundhog.parser.MutableInt;

public class JSONFormater implements Formater {

	/**
	 * Creates an object that represents the JSON result of the metrics.
	 * @return JSONObject that embodies the result
	 */
	@Override
	public String format(HashMap<String, HashMap<String, MutableInt>> object) {
		JSONObject json = new JSONObject();
		try {
			for (Entry<String, HashMap<String, MutableInt>> entry : object
					.entrySet()) {
				json.put(entry.getKey(), entry.getValue());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}
