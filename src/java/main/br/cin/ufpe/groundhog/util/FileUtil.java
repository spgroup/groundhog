package br.cin.ufpe.groundhog.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

public class FileUtil {
	private static FileUtil instance;
	
	public static FileUtil getInstance() {
		if (instance == null) {
			instance = new FileUtil();
		}
		return instance;
	}
	
	private ArrayList<File> createdTempDirs;
	
	private FileUtil() {
		createdTempDirs = new ArrayList<File>();
	}
	
	public synchronized File createTempDir() {
		File tempDir = Files.createTempDir();
		createdTempDirs.add(tempDir);
		return tempDir;
	}
	
	public synchronized void deleteTempDirs() throws IOException {
		for (File tempDir : createdTempDirs) {
			if (tempDir.exists()) {
				FileUtils.deleteDirectory(tempDir);
			}
		}
	}
	
	public void writeStringToFile(File file, String data) throws IOException {
		FileUtils.writeStringToFile(file, data);
	}
	
	public void copyDirectory(File srcDir, File destDir) throws IOException {
		FileUtils.copyDirectory(srcDir, destDir);
	}
	
}