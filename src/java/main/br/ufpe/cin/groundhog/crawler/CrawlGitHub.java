package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.scmclient.GitClient;

import com.google.inject.Inject;

/**
 * A concrete class to crawl GitHub.
 * 
 * @author fjsj
 */
public class CrawlGitHub extends ForgeCrawler {

	private final GitClient gitClient;

	@Inject
	public CrawlGitHub(GitClient gitClient, File destinationFolder) {
		super(destinationFolder);
		this.gitClient = gitClient;
	}

	@Override
	protected File downloadProject(Project project) throws IOException,
			InvalidRemoteException, TransportException, GitAPIException {
		String projectName = project.getName();
		String cloneUrl = project.getScmURL();
		File projectFolder = new File(destinationFolder, projectName);

		gitClient.clone(cloneUrl, projectFolder);
		return projectFolder;
	}
}