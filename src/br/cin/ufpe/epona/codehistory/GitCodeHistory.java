package br.cin.ufpe.epona.codehistory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

import br.cin.ufpe.epona.scmclient.GitClient;

import com.google.common.io.Files;

public class GitCodeHistory implements CodeHistory {
	
	private static GitCodeHistory instance;
	
	public static GitCodeHistory getInstance() {
		if (instance == null) {
			instance = new GitCodeHistory();
		}
		return instance;
	}
	
	private GitCodeHistory() {	
	
	}
	
	@Override
	public File checkoutToDate(String project, String url, Date date) {
		throw new NoSuchMethodError("Not implemented");
	}
	
	@Override
	public File checkoutToDate(String project, File repositoryFolder, final Date date)
			throws IOException, RefAlreadyExistsException, RefNotFoundException,
			InvalidRefNameException, CheckoutConflictException, GitAPIException {
		File projectFolder = new File(Files.createTempDir(), project);
		FileUtils.copyDirectory(repositoryFolder, projectFolder);
		GitClient.getInstance().checkout(projectFolder, date);
		return projectFolder;
	}

	public static void main(String[] args) throws Exception {
		new GitCodeHistory().checkoutToDate("javacv",
				new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\javacv"),
				new GregorianCalendar(2012, 5, 23).getTime());
	}
	
}
