package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.util.ArrayList;

/**
 * Represents a java package in Groundhog metrics extractor
 * @author Bruno Soares, Tulio Lajes, Valdemir Andrade
 * @since 0.1.0
 */

public class JavaPackage {
	
	private ArrayList<JavaFile> files;
	
	private File path;
	
	private String name;

	public JavaPackage(File path, String name){
		this.path = path;
		this.name = name;
		this.files = new ArrayList<JavaFile>();
		detectJavaFiles();
	}

	public JavaPackage(File path){
		this.path = path;
		this.name = path.getName();
		this.files = new ArrayList<JavaFile>();
		detectJavaFiles();
	}

	public JavaPackage(String path, String name){
		this.path = new File(path);
		this.name = name;
		this.files = new ArrayList<JavaFile>();
		detectJavaFiles();
	}

	public JavaPackage(String path){
		this.path = new File(path);
		this.name = this.path.isDirectory() ? this.path.getName() : "";
		this.files = new ArrayList<JavaFile>();
		detectJavaFiles();
	}

	@Override
	public String toString() {
		return "Package: " + this.name;
	}
	
	private void detectJavaFiles(){
		for (File file : this.path.listFiles()){
			if(file.getName().endsWith(".java")){
				this.files.add(new JavaFile(file,file.getName()));
				System.out.println("Java File " + file.getName() + " detected!");
			}
		}
	}
}
