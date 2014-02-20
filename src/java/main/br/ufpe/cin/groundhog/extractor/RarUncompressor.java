package br.ufpe.cin.groundhog.extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

/**
 * @author fjsj, gustavopinto, Rodrigo Alves
 * Adapted from: https://github.com/edmund-wagner/junrar/blob/6f32323c983015d96c64084418793853f514b519/testutil/src/main/java/de/innosystec/unrar/testutil/ExtractArchive.java
 * Original author: Edmund Wagner
 * @deprecated
 */
public class RarUncompressor {
	private static Log logger = LogFactory.getLog(RarUncompressor.class
			.getName());

	/**
	 * Extracts an archive file to a specified destination directory
	 * @param archive the archive file to be extracted
	 * @param destination the directory to which the extracted file will be moved to
	 */
	public static void extract(File archive, File destination) {
		destination.mkdirs();
		Archive arch = null;
		try {
			arch = new Archive(archive);
		} catch (RarException e) {
			logger.error(e);
		} catch (IOException e1) {
			logger.error(e1);
		}
		if (arch != null) {
			if (arch.isEncrypted()) {
				logger.warn("archive is encrypted, cannot perform extraction");
				return;
			}
			FileHeader fh = null;
			while (true) {
				fh = arch.nextFileHeader();
				if (fh == null) {
					break;
				}
				if (fh.isEncrypted()) {
					logger.warn("file is encrypted cannot be extracted: "
							+ fh.getFileNameString());
					continue;
				}
				logger.debug("extracting: " + fh.getFileNameString());
				try {
					if (fh.isDirectory()) {
						createDirectory(fh, destination);
					} else {
						File f = createFile(fh, destination);
						OutputStream stream = new FileOutputStream(f);
						arch.extractFile(fh, stream);
						stream.close();
					}
				} catch (IOException e) {
					logger.error("error extracting the file", e);
				} catch (RarException e) {
					logger.error("error extraction the file", e);
				}
			}
		}
	}

	/**
	 * Creates a file in a specified destination directory
	 * @param fh the file to be created
	 * @param destination the destination to which the newly created file will be moved to
	 * @return
	 */
	private static File createFile(FileHeader fh, File destination) {
		File f = null;
		String name = null;
		if (fh.isFileHeader() && fh.isUnicode()) {
			name = fh.getFileNameW();
		} else {
			name = fh.getFileNameString();
		}
		f = new File(destination, name);
		if (!f.exists()) {
			try {
				f = makeFile(destination, name);
			} catch (IOException e) {
				logger.error("error creating the new file: " + f.getName(), e);
			}
		}
		return f;
	}

	/**
	 * Creates a file in a specified destination folder
	 * @param destination the destination folder
	 * @param name a String indicating the name of the file to be created
	 * @return
	 * @throws IOException thrown when file creation cannot be performed
	 */
	private static File makeFile(File destination, String name)
			throws IOException {
		String[] dirs = name.split("\\\\");
		String path = "";

		if (dirs == null) {
			return null;
		}

		int size = dirs.length;
		if (size == 1) {
			return new File(destination, name);
		} else if (size > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				path = path + File.separator + dirs[i];
				new File(destination, path).mkdir();
			}
			path = path + File.separator + dirs[dirs.length - 1];
			File f = new File(destination, path);
			f.createNewFile();
			return f;
		} else {
			return null;
		}
	}

	/**
	 * Creates a directory within another specified directory
	 * @param fh 
	 * @param destination the directory where the new directory will be placed
	 */
	private static void createDirectory(FileHeader fh, File destination) {
		File f = null;
		if (fh.isDirectory() && fh.isUnicode()) {
			f = new File(destination, fh.getFileNameW());
			if (!f.exists()) {
				makeDirectory(destination, fh.getFileNameW());
			}
		} else if (fh.isDirectory() && !fh.isUnicode()) {
			f = new File(destination, fh.getFileNameString());
			if (!f.exists()) {
				makeDirectory(destination, fh.getFileNameString());
			}
		}
	}

	/**
	 * Creates a directory in an specified destination for every directory within the fileName parameter.
	 * This is done in order to the uncompressed directory to preserve its original file/directory hierarchy.
	 * @param destination the directory that will become the root directory containing the newly created directory.
	 * @param fileName the name of the directory to be created.
	 */
	private static void makeDirectory(File destination, String fileName) {
		String[] dirs = fileName.split("\\\\");
		String path = "";

		if (dirs == null) {
			return;
		}
		
		for (String dir: dirs) {
			path = path + File.separator + dir;
			new File(destination, path).mkdir();
		}
	}
}