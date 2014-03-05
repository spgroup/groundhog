package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
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
	private Statistics statistics = new Statistics();
	
	@Reference(ignoreMissing = true)
	private StatisticsTablePackage table;

	/**
	 * Default constructor used by morphia to create a empty object setting the annotated attributes using reflection 
	 */
	public JavaPackage(){}
	
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

	public JavaPackage(String absolutePath, String name) throws InvalidJavaFileException{
		this.path = new File(absolutePath);
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
