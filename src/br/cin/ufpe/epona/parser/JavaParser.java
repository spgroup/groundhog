package br.cin.ufpe.epona.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that implements the metrics extraction functionality of this project.
 * Works by using the Compiler Tree API (com.sun.source.tree) with JavaCompiler class.
 * @author benitofe, jpso, filipeximenes, weslleyt, fjsj
 *
 */
public class JavaParser {
	private static String fileSeparator = File.separator;
	
	private File folder;
	private List<File> filesList;

	/**
	 * Constructs a new JavaParser which will extract metrics of all Java source files inside
	 * the given folder hierarchy.
	 * @param folder a source folder
	 */
	public JavaParser(File folder) {
		this.folder = folder;
		filesList = new LinkedList<File>();
	}

	private void recursiveSearch(File start) {
		File files[] = start.listFiles();

		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					recursiveSearch(f);
				} else if (f.isFile()) {
					String path = f.getAbsolutePath();
					if (!path.contains(fileSeparator + "__MACOSX") && f.getName().endsWith(".java")) {
						filesList.add(f);	
					}
				}
			}
		}
	}
	
	private HashMap<String, HashMap<String, MutableInt>> invokeProcessor() throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(filesList);
		
		// Options: http://docs.oracle.com/javase/6/docs/technotes/tools/windows/javac.html
		ArrayList<String> options = new ArrayList<String>();
		options.add("-g:none"); // Do not generate any debugging information
		options.add("-nowarn"); // Disable warning messages
		options.add("-implicit:none"); // Suppress class file generation
		options.add("-proc:only"); // Only annotation processing is done, without any subsequent compilation
		// Obs.: only annotation processing is faster and is enough because AST will be built and visited
		
		CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);
		CodeAnalyzerProcessor processor = new CodeAnalyzerProcessor();
		task.setProcessors(Arrays.asList(processor));
		task.call();
		fileManager.close();
		
		return processor.getCounters();
	}
	
	/**
	 * Parses all Java source files inside this.folder and returns extracted metrics.
	 * @return a map of metrics to another map of metric value and count.
	 * @throws IOException if something wrong happens when closing source file manager
	 */
	public HashMap<String, HashMap<String, MutableInt>> parse() throws IOException {
		recursiveSearch(folder);
		if (!filesList.isEmpty()) {
			return invokeProcessor();
		} else {
			return null;
		}
	}
	
	public JSONObject parseToJSON() throws IOException, JSONException {
		JSONObject json = new JSONObject();
		HashMap<String, HashMap<String, MutableInt>> counters = parse();
		if (counters != null) {
			for (Entry<String, HashMap<String, MutableInt>> entry : counters.entrySet()) {
				json.put(entry.getKey(), entry.getValue());
			}
			return json;
		} else {
			return null;
		}
	}
	
	/**
	 * Pretty print metrics. 
	 * @param counters parse method result 
	 */
	public static void printResult(HashMap<String, HashMap<String, MutableInt>> counters) {
		for (String metric : counters.keySet()) {
			System.out.println("Metric - " + metric);
			HashMap<String, MutableInt> counter = counters.get(metric);
			List<Entry<String, MutableInt>> entries = new ArrayList<Entry<String, MutableInt>>();
			entries.addAll(counter.entrySet());
			Collections.sort(entries, new Comparator<Entry<String, MutableInt>>() {
				public int compare(Entry<String, MutableInt> e1, Entry<String, MutableInt> e2) {
					return e2.getValue().get() - e1.getValue().get();
				}
			});
			
			for (Entry<String, MutableInt> entry : entries) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
			System.out.println("-----------------------");
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws Exception {
		HashMap<String, HashMap<String, MutableInt>> counters = 
				new JavaParser(new File("C:\\Users\\fjsj\\AppData\\Local\\Temp\\1341192512523-0\\javacv")).parse();
		printResult(counters);
	}
}
