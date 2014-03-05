package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	
	StatisticsTablePackage table;

	public JavaPackage(File path, String name) throws InvalidJavaFileException{
		this.path = path;
		this.name = name;
		commonInit();
	}

	public JavaPackage(File path) throws InvalidJavaFileException{
		this.path = path;
		this.name = path.getName();
		commonInit();
	}

	public JavaPackage(String path, String name) throws InvalidJavaFileException{
		this.path = new File(path);
		this.name = name;
		commonInit();
	}

	public JavaPackage(String path) throws InvalidJavaFileException{
		this.path = new File(path);
		this.name = this.path.isDirectory() ? this.path.getName() : "";
		commonInit();
	}
	
	public void commonInit() throws InvalidJavaFileException{
		this.files = new ArrayList<JavaFile>();
		this.table = new StatisticsTablePackage();
		detectJavaFiles();
	}

	@Override
	public String toString() {
		return "Package: " + this.name;
	}
	
	public void generateMetrics(GroundhogASTVisitor visitor, MetricsCollector collector){
		
		List<Statistics> stats = new ArrayList<Statistics>();
				
		//Collect metrics
		for(JavaFile file : this.files){
			stats.add(file.generateMetrics(visitor, collector));
		}
		
		collector.processPackageLevel(table, stats);
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
