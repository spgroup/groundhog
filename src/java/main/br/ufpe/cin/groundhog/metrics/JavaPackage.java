package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;

/**
 * Represents a java package in Groundhog metrics extractor
 * @author Bruno Soares, Tulio Lajes, Valdemir Andrade
 * @since 0.1.0
 */

public class JavaPackage {
	
	private ArrayList<JavaFile> files;
	
	private File path;
	
	private String name;
	
	Statistics statistics = new Statistics();

	public JavaPackage(File path, String name) throws InvalidJavaFileException{
		this.path = path;
		this.name = name;
		this.files = new ArrayList<JavaFile>();
		detectJavaFiles();
	}

	public JavaPackage(File path) throws InvalidJavaFileException{
		this.path = path;
		this.name = path.getName();
		this.files = new ArrayList<JavaFile>();
		detectJavaFiles();
	}

	public JavaPackage(String path, String name) throws InvalidJavaFileException{
		this.path = new File(path);
		this.name = name;
		this.files = new ArrayList<JavaFile>();
		detectJavaFiles();
	}

	public JavaPackage(String path) throws InvalidJavaFileException{
		this.path = new File(path);
		this.name = this.path.isDirectory() ? this.path.getName() : "";
		this.files = new ArrayList<JavaFile>();
		detectJavaFiles();
	}

	@Override
	public String toString() {
		return "Package: " + this.name;
	}
	
	public void generateMetrics(GroundhogASTVisitor visitor){
		//Collect metrics
		for(JavaFile file : this.files){
			file.generateMetrics(visitor);
		}
				
		for(JavaFile file : this.files){
			statistics.merge(file.getStat());
		}
		
		MetricsCollector collector = new MetricsCollector();
		//collector.processAll(statistics);
		//System.out.println("============================================================");
	}
	
	private void detectJavaFiles() throws InvalidJavaFileException{
		for (File file : this.path.listFiles()){
			if(file.getName().endsWith(".java")){
				this.files.add(new JavaFile(file,file.getName()));
				System.out.println("Java File " + file.getName() + " detected!");
			}
		}
	}
}
