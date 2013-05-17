package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.util.Random;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.scmclient.GitClient;

import com.google.inject.Inject;

/**
 * A concrete class to crawl GitHub.
 * 
 * @author fjsj, gustavopinto
 */
public class CrawlGitHub extends ForgeCrawler {

	private final static Logger logger = LoggerFactory
			.getLogger(CrawlGitHub.class);

	private final GitClient gitClient;

	@Inject
	public CrawlGitHub(GitClient gitClient, File destinationFolder) {
		super(destinationFolder);
		this.gitClient = gitClient;
	}

	@Override
	protected File downloadProject(Project project)
			throws InvalidRemoteException, TransportException, GitAPIException {
		String projectName = project.getName() + "_" + new Random().nextInt();
		String cloneUrl = project.getScmURL();
		File projectFolder = new File(destinationFolder, projectName);

		logger.info(String.format("Downloading %s project..", project.getName()));

		gitClient.clone(cloneUrl, projectFolder);
		return projectFolder;
	}
}