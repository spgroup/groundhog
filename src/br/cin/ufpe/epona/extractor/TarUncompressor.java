package br.cin.ufpe.epona.extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

public class TarUncompressor {

	// Adapted from:
	// http://java-classes.blogspot.com.br/2012/01/when-you-extract-tar-files-you-can-not.html
	public static void extract(File tarFile, InputStream tarFileInputStream,
			File dir) throws IOException {
		int bufsize = 8192;
		TarArchiveEntry entry;
		TarArchiveInputStream inputStream = new TarArchiveInputStream(
				tarFileInputStream);
		FileOutputStream outputStream = null;

		if (!dir.exists()) {
			dir.mkdirs();
		}
		// For each entry in the tar, extract and save the entry to the file
		// system
		while (null != (entry = inputStream.getNextTarEntry())) {
			// for each entry to be extracted
			int bytesRead;

			// if the entry is a directory, create the directory
			if (entry.isDirectory()) {
				File fileOrDir = new File(dir, entry.getName());
				fileOrDir.mkdir();
				continue;
			}

			// write to file
			byte[] buf = new byte[bufsize];
			outputStream = new FileOutputStream(new File(dir, entry.getName()));
			while ((bytesRead = inputStream.read(buf, 0, bufsize)) > -1)
				outputStream.write(buf, 0, bytesRead);

			try {
				if (null != outputStream) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		inputStream.close();
	}
}