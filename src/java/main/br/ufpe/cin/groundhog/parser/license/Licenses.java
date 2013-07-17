package br.ufpe.cin.groundhog.parser.license;

import java.util.List;

import com.google.common.collect.Lists;

public class Licenses {

	public static List<String> names() {
		return Lists.newArrayList("MIT", "Apache", "GPL-V2", "GPL-V3",
				"No License", "Mozilla", "BSD", "LGPL", "Artistic", "Eclipse",
				"Ruby", "Public Domain", "WTFPL");
	}
}
