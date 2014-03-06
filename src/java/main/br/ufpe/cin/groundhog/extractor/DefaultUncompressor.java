package br.ufpe.cin.groundhog.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import br.ufpe.cin.groundhog.GroundhogException;

/**
 * The default file uncompressor in Groundhog
 * @author fjsj
 * @deprecated
 */
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
	
	/**
	 * The compressed file extractor method that takes a compressed file and a destination folder
	 * and extracts the given file according to its compression format. The supported formats are declared
	 * in the formats class.
	 * @param file the compressed file to be extracted
	 * @param destinationFolder the folder to which the extracted file will be moved to
	 */
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
			throw new GroundhogException("Error when trying to extract the source code", e);
		}
	}

}