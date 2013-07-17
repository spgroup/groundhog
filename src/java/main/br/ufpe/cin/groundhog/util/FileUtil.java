package br.ufpe.cin.groundhog.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.GroundhogException;

import com.google.common.io.Files;

/**
 * General utilities class for file operations
 * 
 * @author fjsj, gustavopinto, Rodrigo Alves
 */
public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	
	private static FileUtil instance;
	private List<File> createdTempDirs;

	public static FileUtil getInstance() {
		if (instance == null) {
			instance = new FileUtil();
		}
		return instance;
	}

	private FileUtil() {
		this.createdTempDirs = new ArrayList<File>();
	}

	/**
	 * 
	 * @return A File object representing the created temporary directory
	 */
	public synchronized File createTempDir() {
		try {
			File tempDir = Files.createTempDir();
			this.createdTempDirs.add(tempDir);
			return tempDir;
		} catch (Exception e) {
			e.printStackTrace();
			String error = "Unable to create temporary directory. Are you running groundhog with admin privileges?";
			logger.error(error);
			throw new GroundhogException(error);
		}
	}

	/**
	 * Deletes the temporary directories used to store the downloaded the
	 * projects.
	 * 
	 * @throws IOException
	 */
	public synchronized void deleteTempDirs() throws IOException {
		for (File tempDir : this.createdTempDirs) {
			if (tempDir.exists()) {
				FileUtils.deleteDirectory(tempDir);
			}
		}
	}

	/**
	 * 
	 * @param file
	 *            the file to which the string will be written to
	 * @param data
	 *            the string to be written to the file
	 * @throws IOException
	 */
	public void writeStringToFile(File file, String data) throws IOException {
		FileUtils.writeStringToFile(file, data);
	}

	/**
	 * Takes a directory and moves it to another directory
	 * 
	 * @param srcDir
	 *            the directory to be moved
	 * @param destDir
	 *            the destination directory
	 * @throws IOException
	 */
	public void copyDirectory(File srcDir, File destDir) throws IOException {
		FileUtils.copyDirectory(srcDir, destDir);
		// TODO: add tests
	}
	
	public String readAllLines(File file) {
		try {
			StringBuffer buffer = new StringBuffer();
			Scanner scanner = new Scanner(file);
			
			while(scanner.hasNextLine()){
				buffer.append(scanner.nextLine());
			}
			
			scanner.close();
			return buffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

			String error = String.format("Unable to read the file (%s) content", file.getAbsoluteFile());
			logger.error(error);
			throw new GroundhogException(error);
		}
	}
	
	public boolean isTextFile(final File file) {
		
		if(file.isDirectory()) {
			return false;
		}
		
		final int BUFFER_SIZE = 10 * 1024;
		boolean isText = true;
		byte[] buffer = new byte[BUFFER_SIZE];

		RandomAccessFile fis = null;
		try {
			fis = new RandomAccessFile(file, "r");
		
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
			return isText;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fis.close();
			} catch (final Throwable th) {
			}
		}
	}
}