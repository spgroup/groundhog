package br.ufpe.cin.groundhog.codehistory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;

import br.ufpe.cin.groundhog.extractor.DefaultExtractor;
import br.ufpe.cin.groundhog.scmclient.EmptyProjectAtDateException;
import br.ufpe.cin.groundhog.util.FileUtil;

/**
 * The code history analysis implementation for SourceForge
 * @author fjsj
 */
public class SFCodeHistory implements CodeHistory {
	
	/**
	 * 
	 * @param project
	 * @param repositoryFolder
	 * @param Date
	 * @throws EmptyProjectAtDateException
	 */
	@Override
	public File checkoutToDate(String project, File repositoryFolder, Date date) throws EmptyProjectAtDateException {
		long dateLong = date.getTime();
		File closest = null;
		Deque<File> stack = new ArrayDeque<File>();
		stack.add(repositoryFolder);
		
		while (!stack.isEmpty()) {
			File f = stack.pollLast();
			if (f.isDirectory()) {
				stack.addAll(Arrays.asList(f.listFiles()));
			} else {
				long modified = f.lastModified();
				if (modified <= dateLong) {
					if (closest == null) {
						closest = f;
					} else {
						long closestModified = closest.lastModified();
						if (Math.abs(dateLong - modified) < Math.abs(dateLong - closestModified)) {
							closest = f;
						}
					} 
				}
			}
		}
		
		if (closest != null) {
			File projectFolder = new File(FileUtil.getInstance().createTempDir(), project);
			DefaultExtractor.getInstance().extractFile(closest, projectFolder);
			return projectFolder;
		} else {
			throw new EmptyProjectAtDateException(new SimpleDateFormat().format(date));
		}
	}
}