package br.cin.ufpe.groundhog.extractor;

import java.io.File;

public interface Extractor {
	public void extractFile(File file, File destinationFolder);

	public void recursiveExtract(File target, File destinationFolder);
}