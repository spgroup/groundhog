package br.ufpe.cin.groundhog.crawler;

import java.io.File;

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

	private final static Logger logger = LoggerFactory.getLogger(CrawlGitHub.class);

	private final GitClient gitClient;
	private final File destinationFolder;

	@Inject
	public CrawlGitHub(GitClient gitClient, File destinationFolder) {
		super();
		this.gitClient = gitClient;
		this.destinationFolder = destinationFolder;
	}

	@Override
	public File downloadProject(Project project) {
		String projectName = project.getName() + "_" + System.currentTimeMillis();
		String projectUrl = project.getScmURL();
		File projectDestinationFolder = new File(destinationFolder, projectName);

		logger.info(String.format("Downloading %s project..", project.getName()));

		try {
			this.gitClient.clone(projectUrl, projectDestinationFolder);
			return projectDestinationFolder;
		} catch (Exception e) {
			String error = String.format("Unable to download %s (%s) project", project.getName(), project.getScmURL());
			logger.error(error);
			throw new DownloadException(error);
		}
	}
}