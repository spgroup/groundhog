package br.cin.ufpe.epona.extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipUncompressor {

	public static void extract(File zipFile, File dir) throws IOException {
		ZipFile zip = null;
		File currentFile = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[1024];

		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!dir.exists() || !dir.isDirectory()) {
				throw new IOException("The directory is no a valid one: "
						+ dir.getName()
						+ ". Check if it exists and it is really a directory.");
			}

			zip = new ZipFile(zipFile);
			Enumeration<? extends ZipEntry> e = zip.entries();

			while (e.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				currentFile = new File(dir, entry.getName());

				// if the directory is inexistent, create the structure and jump
				// to the next entry
				if (entry.isDirectory()) {
					if (!currentFile.exists()) {
						currentFile.mkdirs();
					}
					continue;
				}

				// if the directory is inexistent, create
				if (!currentFile.getParentFile().exists()) {
					currentFile.getParentFile().mkdirs();
				}

				// read the bytes and save to file
				try {
					is = zip.getInputStream(entry);
					os = new FileOutputStream(currentFile);
					int bytesRead = 0;
					if (is == null) {
						throw new ZipException(
								"Error while reading zip entry: "
										+ entry.getName());
					}
					while ((bytesRead = is.read(buffer)) > 0) {
						os.write(buffer, 0, bytesRead);
					}
				} finally {
					if (is != null) {
						is.close();
					}
					if (os != null) {
						os.close();
					}
				}
			}
		} finally {
			if (zip != null) {
				zip.close();
			}
		}
	}
}