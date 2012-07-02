package br.cin.ufpe.epona.extractor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class CompatibleFormats {
	
	private static CompatibleFormats instance;
	private List<String> extensionsList;
	private HashSet<String> extensions;
	
	public static CompatibleFormats getInstance() {
		if (instance == null) {
			instance = new CompatibleFormats();
		}
		return instance;
	}
	
	private CompatibleFormats() {
		String[] extensionsStr = {".zip", ".tar.gz", ".tgz",
				".tar.bz2", ".tar.bzip2", ".tar.lzma", ".tlzma",
				".rar", ".tar"};
		extensions = new HashSet<String>();
		extensionsList = Collections.unmodifiableList(Arrays.asList(extensionsStr));
		extensions.addAll(extensionsList);
	}
	
	public boolean contains(String extension) {
		return extensions.contains(extension);
	}
	
	public List<String> asList() {
		return extensionsList;
	}
	
}
