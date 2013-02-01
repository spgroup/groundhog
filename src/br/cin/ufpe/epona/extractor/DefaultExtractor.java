package br.cin.ufpe.epona.extractor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.cin.ufpe.epona.Config;
import br.cin.ufpe.epona.util.FileUtil;

/**
 * Singleton class to extract files and recursively extract all files within an hierarchy of folders.
 * Supports some of the most popular compression formats: .zip, .tar.gz, .tgz, .tar.bz2, .tar.bzip2, .tar.lzma, .tlzma, .rar, .tar 
 * @author fjsj
 *
 */
public class DefaultExtractor implements Extractor {
	
	private static Log logger = LogFactory.getLog(DefaultExtractor.class.getName());
	
	private static DefaultExtractor instance;
	
	/**
	 * Gets an instance of Extractor.
	 * @return an instance of Extractor
	 */
	public static DefaultExtractor getInstance() {
		if (instance == null) {
			instance = new DefaultExtractor();
		}
		return instance;
	}
	
	private DefaultExtractor() {
		
	}
	
	private void recursiveExtract(final File root, File next, final File destinationFolder) {
		ExecutorService executor = Executors.newFixedThreadPool(Config.MAX_NUMBER_OF_THREADS);
		File[] subFiles = next.listFiles();
		List<Future<?>> futures = new ArrayList<Future<?>>();
		
		if (subFiles != null) {
			for (final File f : subFiles) {
				if (f.isDirectory()) {
					recursiveExtract(root, f, destinationFolder);
				} else if (f.isFile()) {					
					Future<?> future = executor.submit(new Runnable() {
						public void run() {
							String parent = f.getParent();
							extractFile(f, new File(destinationFolder, parent.substring(root.getParent().length())));
						}
					});
					futures.add(future);
				}
			}
		}
		executor.shutdown();
		for (Future<?> f : futures) {
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Recursively extracts an hierarchy of folders to a given destination folder.
	 * The destination will be populated by the same hierarchy of the target, but will have the
	 * originally archived files extracted.
	 * @param target target folder
	 * @param destinationFolder destination folder
	 */
	public void recursiveExtract(File target, File destinationFolder) {
		recursiveExtract(target, target, destinationFolder);
	}
	
	/**
	 * Extracts a given file to a given destination folder.
	 * Logs a warning if it is unable to extract due a unknown compression format. 
	 * @param file target file
	 * @param destinationFolder destination folder
	 */
	public void extractFile(File file, File destinationFolder) {
		if (file.isFile()) {
			String fileName = file.getName();
			boolean extracted = false;
			
			if(Formats.getInstance().isCompatible(fileName)) {
				DefaultUncompressor.getInstance().uncompress(file, destinationFolder);
				extracted = true;
			}
			
			if (!extracted) {
				logger.warn("Unable to extract file (unkwown compression format): " + file.getAbsolutePath());
			}
		}
	}
	
	public static void main(String[] args) {
		File destinationFolder = FileUtil.getInstance().createTempDir();
		System.out.println(destinationFolder);
		DefaultExtractor.getInstance().recursiveExtract(new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\geom-java"), destinationFolder);
	}
}
