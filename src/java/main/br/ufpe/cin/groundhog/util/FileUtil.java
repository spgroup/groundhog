package br.ufpe.cin.groundhog.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import br.ufpe.cin.groundhog.GroundhogException;

import com.google.common.io.Files;

/**
 * General utilities class for file operations
 * @author fjsj, gustavopinto, rodrigoalvesvieira
 */
public class FileUtil {
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
			throw new GroundhogException("Unable to create temporary directory. Are you running groundhog with admin privileges?");
		}
	}
	
	/**
	 * Deletes the temporary directories used to store the
	 * downloaded the projects.
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
	 * @param file the file to which the string will be written to
	 * @param data the string to be written to the file
	 * @throws IOException
	 */
	public void writeStringToFile(File file, String data) throws IOException {
		FileUtils.writeStringToFile(file, data);
	}
	
	/**
	 * Takes a directory and moves it to another directory
	 * 
	 * @param srcDir the directory to be moved
	 * @param destDir the destination directory
	 * @throws IOException
	 */
	public void copyDirectory(File srcDir, File destDir) throws IOException {
		FileUtils.copyDirectory(srcDir, destDir);
		// TODO: add tests
	}
}