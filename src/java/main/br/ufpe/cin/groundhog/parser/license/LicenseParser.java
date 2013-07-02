package br.ufpe.cin.groundhog.parser.license;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.License;
import br.ufpe.cin.groundhog.parser.java.JavaParser;

public class LicenseParser {

	private static Logger logger = LoggerFactory.getLogger(JavaParser.class);

	private final File[] files;

	public LicenseParser(File folder) {
		this.files = folder.listFiles();
	}

	/**
	 * Parses the top level folder looking for licenses files
	 */
	License parser() {
		logger.info("Running license parser..");

		try {
			for (File file : files) {
				if (isText(file)) {
					boolean containsLicense = containLicense(file);
					if(containsLicense) {
						return extractLicense(file);						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new LicenseNotFoundException("No license found for project %s");
	}

	private License extractLicense(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean containLicense(File file) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isText(final File file)
			throws IOException {
		final int BUFFER_SIZE = 10 * 1024;
		boolean isText = true;
		byte[] buffer = new byte[BUFFER_SIZE];

		final RandomAccessFile fis = new RandomAccessFile(file, "r");
		try {
			fis.seek(0);
			final int read = fis.read(buffer);
			int lastByteTranslated = 0;
			for (int i = 0; i < read && isText; i++) {
				final byte b = buffer[i];
				int ub = b & (0xff);
				int utf8value = lastByteTranslated + ub;
				lastByteTranslated = (ub) << 8;

				if (ub == 0x09
						|| ub == 0x0A
						|| ub == 0x0C
						|| ub == 0x0D
						|| (ub >= 0x20 && ub <= 0x7E)
						|| (ub >= 0xA0 && ub <= 0xEE)
						|| (utf8value >= 0x2E2E && utf8value <= 0xC3BF)) {

				} else {
					isText = false;
				}
			}
		} finally {
			try {
				fis.close();
			} catch (final Throwable th) {
			}

		}
		return isText;
	}
}
