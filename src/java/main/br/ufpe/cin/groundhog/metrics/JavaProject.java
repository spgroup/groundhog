package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

import org.apache.commons.lang3.text.translate.CodePointTranslator;

import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;

/**
 * Represents a java project in Groundhog metrics extractor
 * @author Bruno Soares, Tulio Lajes, Valdemir Andrade
 * @since 0.1.0
 */

public class JavaProject {

	public static final String default_source_root_code = "src";

	private ArrayList<JavaPackage> code_packages;
	private ArrayList<JavaPackage> test_packages;

	private File path;

	private File src;

	private File srtc;

	private String name;

	public JavaProject(File path, String name) throws InvalidJavaProjectPathException {
		this.path = path;
		this.name = name;
		checkPath();
		this.code_packages = new ArrayList<JavaPackage>();
		this.test_packages = new ArrayList<JavaPackage>();
	}

	public JavaProject(File path) throws InvalidJavaProjectPathException{
		this.path = path;
		this.name = path.getName();
		checkPath();
		this.code_packages = new ArrayList<JavaPackage>();
		this.test_packages = new ArrayList<JavaPackage>();
	}

	public JavaProject(String path, String name) throws InvalidJavaProjectPathException{
		this.path = new File(path);
		this.name = name;
		checkPath();
		this.code_packages = new ArrayList<JavaPackage>();
		this.test_packages = new ArrayList<JavaPackage>();
	}

	public JavaProject(String path) throws InvalidJavaProjectPathException{
		this.path = new File(path);
		this.name = this.path.isDirectory() ? this.path.getName() : "";
		checkPath();
		this.code_packages = new ArrayList<JavaPackage>();
		this.test_packages = new ArrayList<JavaPackage>();
	}

	private void checkPath() throws InvalidJavaProjectPathException{

		if(!this.path.isDirectory()){

			System.out.println("This project have an invalid path!");
			throw new InvalidJavaProjectPathException();

		}

	}

	public boolean generateStructure(String src, String srtc) throws InvalidJavaProjectPathException, InvalidSourceRootCodePathException, InvalidTestSourcePathException{

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
		String scr = (source_root_code == null ? JavaProject.default_source_root_code : source_root_code);

		File temp_src = new File(this.path.getAbsolutePath(), scr);

		if(temp_src.exists())
			this.src = temp_src;
		else
			throw new InvalidSourceRootCodePathException();

	}

	private void detectSourceRootTestCode(String source_test_root_code) throws InvalidTestSourcePathException{
		System.out.println("Detecting sorce root test code...");

		File temp_srtc = new File(this.path.getAbsolutePath(),source_test_root_code);

		if(temp_srtc.exists())
			this.srtc = temp_srtc;
		else
			throw new InvalidTestSourcePathException();

	}

	@Override
	public String toString() {
		return "Name: " + this.name + "\n"
				+ "SRC: " + this.src.getAbsolutePath() + "\n"
				+ "SRTC: " + this.srtc.getAbsolutePath(); 
	}

	private void detectPackages(File dir,ArrayList<JavaPackage> packages, File src){

		//Check if this project have files on default package
		if(dir.equals(this.src) && hasJavaFiles(dir)){			
			System.out.println("Package default detected!");
			packages.add(new JavaPackage(dir,"default"));
		}

		for(File file : dir.listFiles()){
			//We have a directory and java files, so we have a package
			if(file.isDirectory() && hasJavaFiles(file)){
				System.out.println("Package " + extractPackageName(src,file) + " detected!");
				JavaPackage java_package = new JavaPackage(file,extractPackageName(src,file));
				packages.add(java_package);
				detectPackages(file,packages,src);
			}else if(file.isDirectory()){
				//Search for packages inside actual package
				detectPackages(file,packages,src);			
			}
		}

	}
	
	private void detectCodePackages(){
		if(this.src != null){
			detectPackages(this.src, this.code_packages, this.src);
		}
	}
	
	private void detectTestCodePackages(){
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
}
