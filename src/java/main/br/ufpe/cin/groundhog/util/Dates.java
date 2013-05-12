package br.ufpe.cin.groundhog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {

	public static Date format(String format, String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date formatedDate = sdf.parse(date.replace('T', ' ').replace("Z", ""));
			return formatedDate;
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
}
