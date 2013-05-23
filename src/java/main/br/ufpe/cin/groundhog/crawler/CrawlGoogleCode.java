package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.scmclient.GitClient;

/**
 * A concrete class to crawl GitHub.
 * 
 * @author fjsj
 */
public class CrawlGoogleCode extends ForgeCrawler {
	private static Logger logger = LoggerFactory.getLogger(CrawlGoogleCode.class);
	
	private final GitClient gitClient;
	private final File destinationFolder;
	
	public CrawlGoogleCode(GitClient gitClient, File destinationFolder) {
		super();
		this.gitClient = gitClient;
		this.destinationFolder = destinationFolder;
	}
	
	@Override
	protected File downloadProject(Project project)
			throws IOException, SVNException,
			InvalidRemoteException, TransportException, GitAPIException {
		String projectName = project.getName();
		SCM projectSCM = project.getSCM();
		File projectFolder = new File(destinationFolder, projectName);
		
		switch (projectSCM) {
		case SVN:
			// It is no advantage to checkout now, since SVN version history is stored remotely.
			// CodeHistory shall use checkout the desired version later.
			break;
		case GIT:
			String url = project.getScmURL();
			gitClient.clone(url, projectFolder);
			break;
		case NONE:
			logger.warn(String.format("Project %s has no SCM.", projectName));
			break;
		case HG:
			String scm = project.getSCM().toString();
			logger.warn(String.format("Project %s has a unsupported SCM: %s", projectName, scm));
			break;
		case UNKNOWN:
			scm = project.getSCM().toString();
			logger.warn(String.format("Project %s has a unsupported SCM: %s", projectName, scm));
			break;
		default:
			scm = project.getSCM().toString();
			logger.error(String.format("Project %s doesn't have %s as SCM, which isn't a Google Code compatible SCM. " +
					"Are you sure this project came from Google Code?", projectName, scm));
		}
		return projectFolder;
	}
}