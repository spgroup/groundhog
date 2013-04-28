package br.ufpe.cin.groundhog.codehistory;

import java.io.File;
import java.util.Date;

import com.google.inject.Inject;

import br.ufpe.cin.groundhog.scmclient.EmptyProjectAtDateException;
import br.ufpe.cin.groundhog.scmclient.GitClient;
import br.ufpe.cin.groundhog.util.FileUtil;

public class GitCodeHistory implements CodeHistory {
	
	private final GitClient gitClient;
	
	@Inject
	public GitCodeHistory(GitClient gitClient){
		this.gitClient = gitClient;
	}
	
	@Override
	public File checkoutToDate(String project, String url, Date date) {
		throw new NoSuchMethodError("Not implemented");
	}

	@Override
	public File checkoutToDate(String project, File repositoryFolder,
			final Date date) throws CheckoutException,
			EmptyProjectAtDateException {
		try {
			File projectFolder = new File(FileUtil.getInstance()
					.createTempDir(), project);
			FileUtil.getInstance().copyDirectory(repositoryFolder,
					projectFolder);
			this.gitClient.checkout(projectFolder, date);
			return projectFolder;
		} catch (Exception e) {
			throw new CheckoutException(e);
		}
	}
}