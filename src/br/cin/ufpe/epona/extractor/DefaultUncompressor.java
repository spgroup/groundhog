package br.cin.ufpe.epona.extractor;

import java.io.*;
import java.util.zip.GZIPInputStream;

import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

public class DefaultUncompressor {
	
	private static DefaultUncompressor instance;
	
	public static DefaultUncompressor getInstance() {
		if (instance == null) {
			instance = new DefaultUncompressor();
		}
		return instance;
	}
	
	private DefaultUncompressor() {
		
	}
	
	public void uncompress(File file, File destinationFolder) {
		String name = file.getName();
		try {
			if (name.endsWith(".zip")) {
				ZipUncompressor.extract(file, destinationFolder);
			} else if (name.endsWith(".tar.gz") || name.endsWith(".tgz")) {
				TarUncompressor.extract(file, new GZIPInputStream(new FileInputStream(file)), destinationFolder);
			} else if (name.endsWith(".tar.bz2") || name.endsWith(".tar.bzip2") || name.endsWith(".tbz2")) {
				TarUncompressor.extract(file, new BZip2CompressorInputStream(new FileInputStream(file)), destinationFolder);
			} else if (name.endsWith(".tar.lzma") || name.endsWith(".tlzma")) {
				TarUncompressor.extract(file, new LzmaInputStream(new FileInputStream(file), new Decoder()), destinationFolder);
			} else if (name.endsWith(".rar")) {
				RarUncompressor.extract(file, destinationFolder);
			} else if (name.endsWith(".tar")) {
				TarUncompressor.extract(file, new FileInputStream(file), destinationFolder);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
