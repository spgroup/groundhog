package br.ufpe.cin.groundhog.parser.java.formater;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import au.com.bytecode.opencsv.CSVWriter;
import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.parser.java.MutableInt;

/**
 * Creates the CSV representation of the extracted metrics The output file
 * groups data associated with each metric. Each metric has a set of entry value
 * that are printed one per line
 * 
 * @author drn2
 * @since 0.0.1
 */
public class CSVFormater extends Formater {

	@Override
	public String format(HashMap<String, HashMap<String, MutableInt>> object) {
		StringWriter result = new StringWriter();
		try {
			CSVWriter writer = new CSVWriter(result, ';');
			writer.writeNext(new String[] { "Metric", "Entry", "Value" });
			for (String metric : object.keySet()) {
				HashMap<String, MutableInt> counter = object.get(metric);
				for (Entry<String, MutableInt> entry : counter.entrySet()) {
					writer.writeNext(new String[] { metric, entry.getKey(), entry.getValue().toString() });
				}
			}
			writer.flush();
			writer.close();
			return result.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new GroundhogException();
		}
	}
}
