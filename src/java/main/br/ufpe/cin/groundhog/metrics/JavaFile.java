package br.ufpe.cin.groundhog.metrics;

import java.io.File;

/**
 * Represents a java class in Groundhog metrics extractor
 * @author Bruno Soares, Tulio Lajes, Valdemir Andrade
 * @since 0.1.0
 */

public class JavaFile {
	
	private File path;
	private String name;
	
	public JavaFile(File path, String name){
		this.path = path;
		this.name = name;
	}

	public JavaFile(File path){
		this.path = path;
		this.name = path.getName();
	}

	public JavaFile(String path, String name){
		this.path = new File(path);
		this.name = name;
	}

	public JavaFile(String path){
		this.path = new File(path);
		this.name = this.path.isFile() ? this.path.getName() : "";
	}

	@Override
	public String toString() {
		return "File: " + this.name;
	}
	
	public JavaFile() {
		// TODO Auto-generated constructor stub
	}
	
	public File getFile() {
		return path;
	}

	public void setFile(File file) {
		this.path = file;
	}
	
	
}
