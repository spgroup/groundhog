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
import org.json.JSONObject;

import br.cin.ufpe.epona.entity.ForgeProject;
import br.cin.ufpe.epona.entity.SCM;
import br.cin.ufpe.epona.http.Requests;
import br.cin.ufpe.epona.scm.client.GitClient;

public class CrawlGitHub extends ForgeCrawler {

	private static String root = "https://api.github.com";
	
	public CrawlGitHub(File destinationFolder) {
		super(destinationFolder);
	}
	
	private JSONObject getJsonFromAPI(String urlStr) throws JSONException, IOException {
		return new JSONObject(Requests.getInstance().get(urlStr));
	}
	
	private String getProjectCloneURL(String project) throws JSONException, IOException {
		String searchUrl = root + String.format("/legacy/repos/search/%s?start_page=1&language=java", project);
		JSONObject firstResult = getJsonFromAPI(searchUrl).getJSONArray("repositories").getJSONObject(0);
		String projectRealName = firstResult.getString("name");
		String username = firstResult.getString("username");
		String projectUrl = root + String.format("/repos/%s/%s", username, projectRealName);
		String cloneUrl = getJsonFromAPI(projectUrl).getString("clone_url");
		return cloneUrl;
	}
	
	@Override
	protected File downloadProject(ForgeProject project)
			throws JSONException, IOException, 
			InvalidRemoteException, TransportException, GitAPIException {
		String projectName = project.getName();
		String cloneUrl = getProjectCloneURL(projectName);
		File projectFolder = new File(destinationFolder, projectName);
		
		project.setSCM(SCM.GIT);
		project.setScmURL(cloneUrl);
		GitClient.getInstance().clone(cloneUrl, projectFolder);
		return projectFolder;
	}
	
	public static void main(String[] args) throws Exception {
		long time = System.nanoTime();
		List<ForgeProject> projects = Arrays.asList(new ForgeProject("junit", ""), new ForgeProject("playframework", ""));
		File dest = new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\");
		CrawlGitHub crawl = new CrawlGitHub(dest);
		List<Future<File>> fs = crawl.downloadProjects(projects);
		crawl.shutdown();
		for (Future<File> f : fs) f.get();
		System.out.printf("Elapsed: %.2f", (System.nanoTime() - time) / 1000000000.0);
	}
}
