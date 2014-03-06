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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

import br.ufpe.cin.groundhog.database.GroundhogDB;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;

/**
 * Represents a java package in Groundhog metrics extractor
 * @author Bruno Soares, Tulio Lajes, Valdemir Andrade
 * @since 0.1.0
 */

@Entity("javapackages")
public class JavaPackage {
	
	@Transient
	private static Logger logger = LoggerFactory.getLogger(JavaProject.class);
	
	@Id
	@Indexed(unique=true, dropDups=true)
	ObjectId id;
	
	@Reference
	@SerializedName("javafiles")
	private ArrayList<JavaFile> files;
	
	@Transient
	private File path;
	
	@SerializedName("absolutepath")
	private String absolutePath;
	
	@SerializedName("name")
	private String name;
	
	@Transient
	private Statistics statistics = new Statistics();
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ArrayList<JavaFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<JavaFile> files) {
		this.files = files;
	}

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public StatisticsTablePackage getTable() {
		return table;
	}

	public void setTable(StatisticsTablePackage table) {
		this.table = table;
	}

	@Reference
	@SerializedName("sttablepackage")
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
	
	public List<Statistics> generateMetrics(GroundhogASTVisitor visitor, MetricsCollector collector){
		
		List<Statistics> stats = new ArrayList<Statistics>();
				
		//Collect metrics
		for(JavaFile file : this.files){
			stats.add(file.generateMetrics(visitor, collector));
		}
		
		collector.processPackageLevel(table, stats);
		logger.debug(table.toString());
		return stats;
	}
	
	public List<Statistics> generateMetrics(GroundhogASTVisitor visitor, MetricsCollector collector, GroundhogDB db){
		List<Statistics> stats = new ArrayList<Statistics>();
		
		//Collect metrics
		for(JavaFile file : this.files){
			stats.add(file.generateMetrics(visitor, collector,db));
		}
		
		collector.processPackageLevel(table, stats);
		
		logger.debug(table.toString());
		
		db.save(this.table);
		db.save(this);
		
		return stats;
	}
	
	private void detectJavaFiles() throws InvalidJavaFileException{
		
		this.absolutePath = this.path.getAbsolutePath();
		for (File file : this.path.listFiles()){
			if(file.getName().endsWith(".java")){
				this.files.add(new JavaFile(file,file.getName()));
				logger.debug("Java File " + file.getName() + " detected!");
			}
		}
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
}
