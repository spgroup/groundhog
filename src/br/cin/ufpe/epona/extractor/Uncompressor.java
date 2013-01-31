package br.cin.ufpe.epona.extractor;

import java.io.File;


public interface Uncompressor {

	public Type getType();
	
	public void uncompress(File file);

}
