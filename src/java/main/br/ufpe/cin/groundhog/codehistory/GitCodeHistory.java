package br.ufpe.cin.groundhog.codehistory;

import java.io.File;
import java.util.Date;

import br.ufpe.cin.groundhog.scmclient.EmptyProjectAtDateException;
import br.ufpe.cin.groundhog.scmclient.GitClient;
import br.ufpe.cin.groundhog.util.FileUtil;

public class GitCodeHistory implements CodeHistory {
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
			GitClient.getInstance().checkout(projectFolder, date);
			return projectFolder;
		} catch (Exception e) {
			throw new CheckoutException(e);
		}
	}
}