package br.ufpe.cin.groundhog.parser.formater;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import au.com.bytecode.opencsv.CSVWriter;
import br.ufpe.cin.groundhog.parser.MutableInt;

public class CSVFormater implements Formater {

	/**
	 * Creates the CSV representation of the extracted metrics The output file
	 * groups data associated with each metric. Each metric has a set of entry
	 * value that are printed one per line
	 * 
	 * @return String that represents the CSV document
	 */
	@Override
	public String format(HashMap<String, HashMap<String, MutableInt>> object) {
		StringWriter result = new StringWriter();
		try {
			CSVWriter writer = new CSVWriter(result, ';');
			writer.writeNext(new String[] { "Metric", "Entry", "Value" });
			for (String metric : object.keySet()) {
				HashMap<String, MutableInt> counter = object.get(metric);
				for (Entry<String, MutableInt> entry : counter.entrySet()) {
					writer.writeNext(new String[] { metric, entry.getKey(),
							entry.getValue().toString() });
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
