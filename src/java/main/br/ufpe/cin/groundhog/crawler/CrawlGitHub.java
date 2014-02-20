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
		this.gitClient = gitClient;
		this.destinationFolder = destinationFolder;
	}

	@Override
	public File downloadProject(Project project) throws DownloadException {
		String projectName = project.getName();
		String projectUrl = project.getCheckoutURL();
		File projectDestinationFolder = new File(destinationFolder, projectName);

		logger.info(String.format("Downloading %s project..", project.getName()));

		try {
			this.gitClient.clone(projectUrl, projectDestinationFolder);
			logger.info(String.format("Done! The project is available at %s", projectDestinationFolder.getAbsolutePath()));
			return projectDestinationFolder;
		} catch (Exception e) {
			String error = String.format("Unable to download %s (%s) project", project.getName(), project.getCheckoutURL());
			logger.error(error);
			throw new DownloadException(error);
		}
	}
}