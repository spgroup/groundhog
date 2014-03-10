package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

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

	@Transient
	private static Logger logger = LoggerFactory.getLogger(JavaProject.class);

	@Id
	@Indexed(unique=true, dropDups=true)
	private ObjectId id;

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

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ArrayList<JavaPackage> getCode_packages() {
		return code_packages;
	}

	public void setCode_packages(ArrayList<JavaPackage> code_packages) {
		this.code_packages = code_packages;
	}

	public ArrayList<JavaPackage> getTest_packages() {
		return test_packages;
	}

	public void setTest_packages(ArrayList<JavaPackage> test_packages) {
		this.test_packages = test_packages;
	}

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}

	public File getSrc() {
		return src;
	}

	public void setSrc(File src) {
		this.src = src;
	}

	public File getSrtc() {
		return srtc;
	}

	public void setSrtc(File srtc) {
		this.srtc = srtc;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GroundhogASTVisitor getVisitor() {
		return visitor;
	}

	public void setVisitor(GroundhogASTVisitor visitor) {
		this.visitor = visitor;
	}

	public StatisticsTable getSt_code() {
		return st_code;
	}

	public void setSt_code(StatisticsTable st_code) {
		this.st_code = st_code;
	}

	public StatisticsTable getSt_test() {
		return st_test;
	}

	public void setSt_test(StatisticsTable st_test) {
		this.st_test = st_test;
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public MetricsCollector getCollector() {
		return collector;
	}

	public void setCollector(MetricsCollector collector) {
		this.collector = collector;
	}

	public static String getDefaultSourceRootCode() {
		return default_source_root_code;
	}

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

			logger.error("This project has an invalid path!");

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

	public boolean generateStructure(File src, File srtc) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException, InvalidTestSourcePathException, InvalidJavaFileException{

		if(this.path == null || !this.path.isDirectory())
			return false;
		else{

			detectSourceRootCode(src);
			detectCodePackages();

			if(srtc != null){
				detectSourceRootTestCode(srtc);
				detectTestCodePackages();
			}

			return true;
		}
	}

	private void detectSourceRootCode(String source_root_code) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException{
		logger.info("Detecting sorce root code...");
		//This line has been commented because we need to decide how to detect the correct java source root code
		//String scr = (source_root_code == null ? JavaProject.default_source_root_code : source_root_code);

		File temp_src = null;
		String src = source_root_code;

		temp_src = new File(this.path.getAbsolutePath(), src);

		detectSourceRootCode(temp_src);

	}

	private void detectSourceRootCode(File source_root_code) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException{

		try{	
			if(source_root_code.exists())
				this.src = source_root_code;
			else
				throw new InvalidSourceRootCodePathException();
		}catch (NullPointerException e){
			System.err.println("Source root code not found!");
		}
	}

	private void detectSourceRootTestCode(String source_test_root_code) throws InvalidTestSourcePathException{
		logger.info("Detecting sorce root test code...");

		File temp_srtc = null;

		temp_srtc = new File(this.path.getAbsolutePath(),source_test_root_code);

		detectSourceRootTestCode(temp_srtc);
	}

	private void detectSourceRootTestCode(File source_test_root_code) throws InvalidTestSourcePathException{

		try{	
			if(source_test_root_code.exists())
				this.srtc = source_test_root_code;
			else
				throw new InvalidTestSourcePathException();
		}catch (NullPointerException e){
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
			logger.debug("Package default detected!");
			packages.add(new JavaPackage(dir,"default"));
		}

		for(File file : dir.listFiles()){
			//We have a directory and java files, so we have a package
			if(file.isDirectory() && hasJavaFiles(file)){
				logger.debug("Package " + extractPackageName(src,file) + " detected!");
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

	public boolean generateMetrics(boolean include_tests){

		//For each code package of this project generate their metrics
		for (JavaPackage _package : this.code_packages){
			_package.generateMetrics(this.visitor, this.collector);
		}

		logger.info("All code packages done!");

		if(include_tests){
			for (JavaPackage _package : this.test_packages){
				_package.generateMetrics(this.visitor,this.collector);
			}
		}

		logger.info("All test packages done!");

		return true;
	}

	public boolean generateMetrics(boolean include_tests, GroundhogDB db){

		//For each code package of this project generate their metrics
		for (JavaPackage _package : this.code_packages){
			_package.generateMetrics(this.visitor, this.collector,db);
		}

		logger.info("All code packages done!");

		if(include_tests){

			for (JavaPackage _package : this.test_packages){
				_package.generateMetrics(this.visitor,this.collector,db);
			}

			logger.info("All test packages done!");
		}

		db.save(this);

		return true;
	}
}
