package br.cin.ufpe.epona.crawler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

import br.cin.ufpe.epona.entity.ForgeProject;
import br.cin.ufpe.epona.entity.SCM;
import br.cin.ufpe.epona.http.Requests;
import br.cin.ufpe.epona.scmclient.GitClient;

public class CrawlGoogleCode extends ForgeCrawler {
	
	private static Logger logger = LoggerFactory.getLogger(CrawlGoogleCode.class);
	
	public CrawlGoogleCode(File destinationFolder) {
		super(destinationFolder);
	}
	
	private String getCheckoutCommand(String project) throws IOException {
		Document doc = Jsoup.parse(Requests.getInstance().get(String.format("http://code.google.com/p/%s/source/checkout", project)));
		Elements es = doc.select("#checkoutcmd");
		if (es.isEmpty()) {
			return "";
		} else {
			return es.first().text();
		}
	}
	
	@Override
	protected File downloadProject(ForgeProject project)
			throws IOException, SVNException,
			InvalidRemoteException, TransportException, GitAPIException {
		String projectName = project.getName();
		String command = getCheckoutCommand(projectName);
		File projectFolder = new File(destinationFolder, projectName);
		
		if (command.startsWith("svn")) {
			String url = command.split(" ")[2];
			project.setSCM(SCM.SVN);
			project.setScmURL(url);
			// It is no advantage to checkout now, since SVN version history is stored remotely.
			// The above line has set the SVN URL to ForgeProject and
			// CodeHistory shall use this URL to checkout the desired version later.
			//SVNClient.getInstance().checkout(url, projectFolder); 
		} else if (command.startsWith("git")) {
			String url = command.split(" ")[2];
			project.setSCM(SCM.GIT);
			project.setScmURL(url);
			GitClient.getInstance().clone(url, projectFolder);
		} else if (command.equals("")) {
			project.setSCM(SCM.NONE);
			logger.info(String.format("Project %s has no SCM.", projectName));
		} else {
			project.setSCM(SCM.UNKNOWN);
			String scm = command.split(" ")[0];
			logger.info(String.format("Project %s has a unsupported SCM: %s", projectName, scm));
		}
		return projectFolder;
	}
	
	public static void main(String[] args) throws Exception {
		long time = System.nanoTime();
		List<ForgeProject> projects = Arrays.asList(
				new ForgeProject("epubcheck", ""));
		File dest = new File("C:\\Users\\fjsj\\Downloads\\EponaProjects\\");
		CrawlGoogleCode crawl = new CrawlGoogleCode(dest);
		List<Future<File>> fs = crawl.downloadProjects(projects);
		crawl.shutdown();
		for (Future<File> f : fs) f.get();
		System.out.printf("Elapsed: %.2f", (System.nanoTime() - time) / 1000000000.0);
	}
}
