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

public class SFCodeHistory implements CodeHistory {
	private static SFCodeHistory instance;
	
	public static SFCodeHistory getInstance() {
		if (instance == null) {
			instance = new SFCodeHistory();
		}
		return instance;
	}
	
	private SFCodeHistory() {	
	
	}
	
	@Override
	public File checkoutToDate(String project, String url, Date date) {
		throw new NoSuchMethodError("Not implemented");
	}

	// TODO: how to know if the single extracted file is really the source code of the project
	// and isn't a library or just a part?
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