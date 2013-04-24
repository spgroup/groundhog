package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.json.JSONException;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.scmclient.GitClient;

import com.google.inject.Inject;

public class CrawlGitHub extends ForgeCrawler {

	@Inject
	public CrawlGitHub(File destinationFolder) {
		super(destinationFolder);
	}
	
	@Override
	protected File downloadProject(Project project)
			throws JSONException, IOException, 
			InvalidRemoteException, TransportException, GitAPIException {
		String projectName = project.getName();
		String cloneUrl = project.getScmURL();
		File projectFolder = new File(destinationFolder, projectName);
		
		GitClient.getInstance().clone(cloneUrl, projectFolder);
		return projectFolder;
	}
}