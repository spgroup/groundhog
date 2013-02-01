package br.cin.ufpe.epona.crawler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.json.JSONException;

import br.cin.ufpe.epona.Project;
import br.cin.ufpe.epona.scmclient.GitClient;
import br.cin.ufpe.epona.search.SearchGitHub;

public class CrawlGitHub extends ForgeCrawler {

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
	
	public static void main(String[] args) throws Exception {
		long time = System.nanoTime();
		Project junit = SearchGitHub.getInstance().getProjects("junit", 1).get(0);
		Project playframework = SearchGitHub.getInstance().getProjects("playframework", 1).get(0);
		List<Project> projects = Arrays.asList(junit, playframework);
		File dest = new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\");
		CrawlGitHub crawl = new CrawlGitHub(dest);
		List<Future<File>> fs = crawl.downloadProjects(projects);
		crawl.shutdown();
		for (Future<File> f : fs) f.get();
		System.out.printf("Elapsed: %.2f", (System.nanoTime() - time) / 1000000000.0);
	}
}
