package br.ufpe.cin.groundhog.parser.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.parser.Parser;
import br.ufpe.cin.groundhog.parser.ParserException;

/**
 * Class that implements the metrics extraction functionality of this project.
 * Works by using the Compiler Tree API (com.sun.source.tree) with JavaCompiler
 * class.
 * 
 * @author benitofe, jpso, filipeximenes, weslleyt, fjsj, gustavopinto
 * @since 0.0.1
 * 
 */
public class JavaParser implements Parser<HashMap<String, HashMap<String, MutableInt>>> {
	
	private static Logger logger = LoggerFactory.getLogger(JavaParser.class);
	
	private final File folder;  // this folder means the root folder of the downloaded project
	private final List<File> filesList;

	/**
	 * Constructs a new JavaParser which will extract metrics of all Java source
	 * files inside the given folder hierarchy.
	 * 
	 * @param folder
	 *            a source folder
	 */
	public JavaParser(File folder) {
		this.folder = folder;
		this.filesList = new LinkedList<File>();
	}

	private void searchForJavaFiles(File root) {
		File files[] = root.listFiles();

		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					searchForJavaFiles(f);
				} else if (f.isFile()) {
					String path = f.getAbsolutePath();
					if (!path.contains(File.separator + "__MACOSX") && f.getName().endsWith(".java")) {
						filesList.add(f);	
					}
				}
			}
		}
	}
	
	private HashMap<String, HashMap<String, MutableInt>> invokeProcessor() throws IOException {
		if (!filesList.isEmpty()) {
			return new HashMap<String, HashMap<String, MutableInt>> ();
		} 
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(filesList);
		
		List<String> options = new ArrayList<String>();
		options.add("-g:none"); // Do not generate any debugging information
		options.add("-nowarn"); // Disable warning messages
		options.add("-implicit:none"); // Suppress class file generation
		options.add("-proc:only"); // Only annotation processing is done, without any subsequent compilation
		
		CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);
		CodeAnalyzerProcessor processor = new CodeAnalyzerProcessor();
		task.setProcessors(Arrays.asList(processor));
		task.call();
		fileManager.close();
		
		return processor.getCounters();
	}
	
	/**
	 * Parses all Java source files inside this.folder and returns extracted
	 * metrics.
	 * 
	 * @return a map of metrics to another map of metric value and count.
	 * @throws IOException
	 *             if something wrong happens when closing source file manager
	 */
	public HashMap<String, HashMap<String, MutableInt>> parser() {
		logger.info("Running java parser..");
		searchForJavaFiles(folder);
		try {
			return invokeProcessor();
		} catch (IOException e) {
			throw new ParserException(e.getMessage());
		}
	}
}