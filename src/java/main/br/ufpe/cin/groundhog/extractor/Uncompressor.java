package br.ufpe.cin.groundhog.extractor;

import java.io.File;

@Deprecated
public interface Uncompressor {
	public Type getType();
	public void uncompress(File file);
}