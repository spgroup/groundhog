package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import com.google.gson.annotations.SerializedName;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;

/**
 * Represents a java project in Groundhog metrics extractor
 * @author Bruno Soares, Tulio Lajes, Valdemir Andrade
 * @since 0.1.0
 */

@Entity("javaprojects")
public class JavaProject {

	public static final String default_source_root_code = "src";

	@Reference
	private ArrayList<JavaPackage> code_packages;
	
	@Reference
	private ArrayList<JavaPackage> test_packages;

	@Transient
	private File path;

	@Transient
	private File src;

	@Transient
	private File srtc;

	@SerializedName("absolutepath")
	private String absolutePath;
	
	@SerializedName("name")
	private String name;

	@Transient
	private GroundhogASTVisitor visitor;
	
	@Transient
	private StatisticsTable st_code;
	
	@Transient
	private StatisticsTable st_test;
	
	@Transient
	private Statistics statistics = new Statistics();
	
	@Transient
	private MetricsCollector collector;
	
	/**
	 * Default constructor used by morphia to create a empty object setting the annotated attributes using reflection 
	 */
	public JavaProject() {}
	
	public JavaProject(File path, String name) throws InvalidJavaProjectPathException {

		this.path = path;
		this.name = name;
		commonInit();
	}

	public JavaProject(File path) throws InvalidJavaProjectPathException{

		this.path = path;
		this.name = path.getName();
		commonInit();
	}

	public JavaProject(String absolutePath, String name) throws InvalidJavaProjectPathException{

		this.path = new File(absolutePath);
		this.name = name;
		commonInit();
	}

	public JavaProject(String path) throws InvalidJavaProjectPathException{

		this.path = new File(path);
		this.name = this.path.isDirectory() ? this.path.getName() : "";
		commonInit();
	}

	private void commonInit() throws InvalidJavaProjectPathException{
		
		checkPath();
		this.absolutePath = this.path.getAbsolutePath();
		this.collector = new MetricsCollector();
		this.code_packages = new ArrayList<JavaPackage>();
		this.test_packages = new ArrayList<JavaPackage>();
		this.visitor = new GroundhogASTVisitor();
	}

	private void checkPath() throws InvalidJavaProjectPathException{

		if(!this.path.isDirectory()){

			System.out.println("This project have an invalid path!");
			throw new InvalidJavaProjectPathException();
		}
	}

	public boolean generateStructure(String src, String srtc) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException, InvalidTestSourcePathException, InvalidJavaFileException{

		if(this.path == null || !this.path.isDirectory())
			return false;
		else{
			detectSourceRootCode(src);
			detectSourceRootTestCode(srtc);
			detectCodePackages();
			detectTestCodePackages();
			return true;
		}
	}

	private void detectSourceRootCode(String source_root_code) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException{
		System.out.println("Detecting sorce root code...");
		//This line has been commented because we need to decide how to detect the correct java source root code
		//String scr = (source_root_code == null ? JavaProject.default_source_root_code : source_root_code);
		
		File temp_src = null;
		String src = source_root_code;
		
		try{
			
			temp_src = new File(this.path.getAbsolutePath(), src);

			if(temp_src.exists())
				this.src = temp_src;
			else
				throw new InvalidSourceRootCodePathException();
		}catch (NullPointerException e){
			System.err.println("Source root code not found!");
		}


	}

	private void detectSourceRootTestCode(String source_test_root_code) throws InvalidTestSourcePathException{
		System.out.println("Detecting sorce root test code...");

		File temp_srtc = null;
		
		try{
			
			temp_srtc = new File(this.path.getAbsolutePath(),source_test_root_code);
			
			if(temp_srtc.exists())
				this.srtc = temp_srtc;
			else
				throw new InvalidTestSourcePathException();
		}catch(NullPointerException e){
			System.err.println("Source root test code not found!");
		}
		
		

	}

	@Override
	public String toString() {
		return "Name: " + this.name + "\n"
				+ "SRC: " + ((this.src == null) ? "not found\n" : (this.src.getAbsolutePath() + "\n"))
				+ "SRTC: " + ((this.srtc == null) ? "not found" : this.srtc.getAbsolutePath()); 
	}

	private void detectPackages(File dir,ArrayList<JavaPackage> packages, File src) throws InvalidJavaFileException{

		//Check if this project have files on default package
		if(dir.equals(this.src) && hasJavaFiles(dir)){			
			//System.out.println("Package default detected!");
			packages.add(new JavaPackage(dir,"default"));
		}

		for(File file : dir.listFiles()){
			//We have a directory and java files, so we have a package
			if(file.isDirectory() && hasJavaFiles(file)){
				//System.out.println("Package " + extractPackageName(src,file) + " detected!");
				JavaPackage java_package = new JavaPackage(file,extractPackageName(src,file));
				packages.add(java_package);
				detectPackages(file,packages,src);
			}else if(file.isDirectory()){
				//Search for packages inside actual package
				detectPackages(file,packages,src);			
			}
		}

	}

	private void detectCodePackages() throws InvalidJavaFileException{
		if(this.src != null){
			detectPackages(this.src, this.code_packages, this.src);
		}
	}

	private void detectTestCodePackages() throws InvalidJavaFileException{
		if(this.srtc != null){
			detectPackages(this.srtc, this.test_packages, this.srtc);
		}
	}

	private boolean hasJavaFiles(File dir){

		for (File file : dir.listFiles()){
			if(file.getName().endsWith(".java")) return true;
		}

		return false;
	}

	private String extractPackageName(File src, File dir){
		return dir.getAbsolutePath().
				replace(src.getAbsolutePath()+File.separator, "")
				.replaceAll(Matcher.quoteReplacement(File.separator), ".");
	}

	public void generateMetrics(boolean include_tests){
		//For each code package of this project generate their metrics
		for (JavaPackage _package : this.code_packages){
			_package.generateMetrics(this.visitor, this.collector);
		}
				
		System.out.println("All code packages done!");
		
		if(include_tests){
			for (JavaPackage _package : this.test_packages){
				_package.generateMetrics(this.visitor,this.collector);
			}
		}
		
		System.out.println("All test packages done!");
		
	}
}
