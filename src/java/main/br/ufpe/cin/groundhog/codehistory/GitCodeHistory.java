package br.ufpe.cin.groundhog.codehistory;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.scmclient.EmptyProjectAtDateException;
import br.ufpe.cin.groundhog.scmclient.GitClient;
import br.ufpe.cin.groundhog.util.FileUtil;

import com.google.inject.Inject;

/**
 * The code history analysis implementation for the Git SCM
 * @author fjsj, gustavopinto
 */
public class GitCodeHistory implements CodeHistory {
	
	private static Logger logger = LoggerFactory.getLogger(GitCodeHistory.class);
	
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
	/**
	 * Creates a directory with the name of the project, moves the content of the repository folder into it
	 * and performs the SCM checkout on this new directory
	 * @param project the name of the project
	 * @param repositoryFolder the repository where the source code is located
	 * @param date the date on which the checkout will be based
	 */
	public File checkoutToDate(String project, File repositoryFolder, final Date date) {
		try {
			File projectFolder = new File(FileUtil.getInstance()
					.createTempDir(), project);
			
			FileUtil.getInstance().copyDirectory(repositoryFolder,
					projectFolder);
			
			logger.info("Checking out to the specific date..");
			this.gitClient.checkout(projectFolder, date);
			return projectFolder;
		} catch (EmptyProjectAtDateException e) {
			logger.error(e.getMessage());
			throw new CheckoutException(e.getMessage());
		} catch (Exception e) {
			throw new CheckoutException(e.getMessage());
		}
	}
}