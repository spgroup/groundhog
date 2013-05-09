package br.ufpe.cin.groundhog.parser.formater;

import br.ufpe.cin.groundhog.parser.UnsupportedMetricsFormatException;

public class FormaterFactory {

	public static Formater get(String format) {
		if(format.equals("CSV")) {
			return new CSVFormater();
		} else if(format.equals("JSON")) {
			return new JSONFormater();
		} else {
			String msg = String.format("I did not reconginze this output format (%s) :( I can only format in CSV or JSON", format);
			throw new UnsupportedMetricsFormatException(msg);
		}
	}
}
