package br.cin.ufpe.epona.extractor;

import java.util.Arrays;
import java.util.List;

public class Formats {

	private static Formats instance;
	private List<String> extensions;

	public static Formats getInstance() {
		if (instance == null) {
			instance = new Formats();
		}
		return instance;
	}

	private Formats() {
		String[] extensionsStr = { ".zip", ".tar.gz", ".tgz", ".tar.bz2",
				".tar.bzip2", ".tar.lzma", ".tlzma", ".rar", ".tar" };
		extensions = Arrays.asList(extensionsStr);
	}

	public boolean isCompatible(String extension) {
		for (String ext : extensions) {
			if (extension.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
}
