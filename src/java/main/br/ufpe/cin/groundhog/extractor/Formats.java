package br.ufpe.cin.groundhog.extractor;

import java.util.Arrays;
import java.util.List;

/**
 * This class verifies if a given file format is supported to extraction
 * 
 * @author gustavopinto
 * @deprecated
 */
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
		this.extensions = Arrays.asList(extensionsStr);
	}

	/**
	 * Tell if the given extension is supported
	 * @param extension the given extension String (e.g. zip, rar)
	 */
	public boolean isCompatible(String extension) {
		for (String ext : extensions) {
			if (extension.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
}