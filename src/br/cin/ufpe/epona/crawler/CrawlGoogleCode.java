package br.cin.ufpe.epona.crawler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

import br.cin.ufpe.epona.Project;
import br.cin.ufpe.epona.SCM;
import br.cin.ufpe.epona.scmclient.GitClient;

public class CrawlGoogleCode extends ForgeCrawler {
	
	private static Logger logger = LoggerFactory.getLogger(CrawlGoogleCode.class);
	
	public CrawlGoogleCode(File destinationFolder) {
		super(destinationFolder);
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
			GitClient.getInstance().clone(url, projectFolder);
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
	
	public static void main(String[] args) throws Exception {
		long time = System.nanoTime();
		List<Project> projects = Arrays.asList(
				new Project("epubcheck", ""));
		File dest = new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\");
		CrawlGoogleCode crawl = new CrawlGoogleCode(dest);
		List<Future<File>> fs = crawl.downloadProjects(projects);
		crawl.shutdown();
		for (Future<File> f : fs) f.get();
		System.out.printf("Elapsed: %.2f", (System.nanoTime() - time) / 1000000000.0);
	}
}
