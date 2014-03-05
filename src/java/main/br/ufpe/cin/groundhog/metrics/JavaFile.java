package br.ufpe.cin.groundhog.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;

/**
 * Represents a java class in Groundhog metrics extractor
 * @author Bruno Soares, Tulio Lajes, Valdemir Andrade
 * @since 0.1.0
 */

public class JavaFile {
	
	private File path;
	private String name;
	private ASTParser parser;
	private Scanner scanner;
	private CompilationUnit cu;
	private Statistics stat;
	
	private StatisticsTableFile table;
	
	public Statistics getStat() {
		return stat;
	}

	public JavaFile(File path, String name) throws InvalidJavaFileException{
		
		this.path = path;
		this.name = name;
		commonInit();
	}

	public JavaFile(File path) throws InvalidJavaFileException{
		
		this.path = path;
		this.name = path.getName();
		commonInit();
	}

	public JavaFile(String path, String name) throws InvalidJavaFileException{
		
		this.path = new File(path);
		this.name = name;
		commonInit();
		
	}

	public JavaFile(String path) throws InvalidJavaFileException{
		
		this.path = new File(path);
		this.name = this.path.isFile() ? this.path.getName() : "";
		commonInit();
	}
	
	private void commonInit() throws InvalidJavaFileException{
		
		//Generate statistics structure
		this.stat = new Statistics();
		this.stat.compilationUnits++;
				
		try{
					
			//Read java file
			this.scanner = new Scanner(this.path);
			this.scanner.useDelimiter("\\Z");
			
			//Generate AST
			this.parser = ASTParser.newParser(AST.JLS3);
			String source = this.scanner.next();
			this.parser.setSource(source.toCharArray());
			
			//Close scanner 
			this.scanner.close();
			
			//Generate compilation unit to be visited
			this.cu = (CompilationUnit) parser.createAST(null);
			this.stat.totalCode = Util.countCodeLines(source);
		}catch(FileNotFoundException e){
			throw new InvalidJavaFileException();
		}
	}
	
	@Override
	public String toString() {
		return "File: " + this.name;
	}
	
	public File getFile() {
		return path;
	}

	public void setFile(File file) {
		this.path = file;
	}
		
	public Statistics generateMetrics(GroundhogASTVisitor visitor, MetricsCollector collector){
		visitor.setStatistics(this.stat);
		this.cu.accept(visitor);
		this.stat = visitor.getStatistics();
		collector.processFileLevel(table, stat);
		return this.stat;
	}
	
}
