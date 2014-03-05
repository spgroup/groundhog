package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import com.google.gson.annotations.SerializedName;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;

/**
 * Represents a java package in Groundhog metrics extractor
 * @author Bruno Soares, Tulio Lajes, Valdemir Andrade
 * @since 0.1.0
 */

@Entity("javapackages")
public class JavaPackage {
	
	@Id
	@Indexed(unique=true, dropDups=true)
	ObjectId id;
	
	@Reference
	private ArrayList<JavaFile> files;
	
	@Transient
	private File path;
	
	@SerializedName("absolutepath")
	private String absolutePath;
	
	@SerializedName("name")
	private String name;
	
	@Transient
	Statistics statistics = new Statistics();

	public JavaPackage(){}
	
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

	public JavaPackage(String absolutePath, String name) throws InvalidJavaFileException{
		this.path = new File(absolutePath);
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
		
		this.absolutePath = this.path.getAbsolutePath();
		for (File file : this.path.listFiles()){
			if(file.getName().endsWith(".java")){
				this.files.add(new JavaFile(file,file.getName()));
				System.out.println("Java File " + file.getName() + " detected!");
			}
		}
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public void storeData() {
		
		/**
		 * Stores all the collected data from JavaFiles, JavaPackages, JavaProjects 
		 * and its statistics in the local database
		 */
	}
}
