package br.ufpe.cin.groundhog.parser.license;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Class of most well-known licenses.
 * 
 * @author ghlp
 * @since 0.1.0
 */
public class Licenses {

	public static List<String> names() {
		return Lists.newArrayList("MIT", "Apache", "GPL-*", "LGPL", "Mozilla",
				"BSD", "Artistic", "Eclipse", "Ruby", "Public Domain",
				"No License", "WTFPL");
	}
}
