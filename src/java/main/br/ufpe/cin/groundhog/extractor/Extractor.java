package br.ufpe.cin.groundhog.extractor;

import java.io.File;

@Deprecated
public interface Extractor {
	public void extractFile(File file, File destinationFolder);
	public void recursiveExtract(File target, File destinationFolder);
}