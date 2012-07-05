package br.cin.ufpe.epona.main;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.ufpe.epona.codehistory.CheckoutException;
import br.cin.ufpe.epona.codehistory.CodeHistory;
import br.cin.ufpe.epona.codehistory.GitCodeHistory;
import br.cin.ufpe.epona.codehistory.SFCodeHistory;
import br.cin.ufpe.epona.codehistory.SvnCodeHistory;
import br.cin.ufpe.epona.codehistory.UnsupportedSCMException;
import br.cin.ufpe.epona.crawler.CrawlGitHub;
import br.cin.ufpe.epona.crawler.CrawlGoogleCode;
import br.cin.ufpe.epona.crawler.CrawlSourceForge;
import br.cin.ufpe.epona.crawler.ForgeCrawler;
import br.cin.ufpe.epona.entity.ForgeProject;
import br.cin.ufpe.epona.entity.SCM;
import br.cin.ufpe.epona.http.Requests;
import br.cin.ufpe.epona.parser.JavaParser;
import br.cin.ufpe.epona.scmclient.EmptyProjectAtDateException;
import br.cin.ufpe.epona.scmclient.SVNClient;
import br.cin.ufpe.epona.search.ForgeSearch;
import br.cin.ufpe.epona.search.SearchException;
import br.cin.ufpe.epona.search.SearchGitHub;
import br.cin.ufpe.epona.search.SearchGoogleCode;
import br.cin.ufpe.epona.search.SearchSourceForge;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class CmdMain {

	private static Logger logger = LoggerFactory.getLogger(CrawlGoogleCode.class);
	
	private static String f(String s, Object... args) {
		return String.format(s, args);
	}
	
	public static ForgeSearch defineForgeSearch(SupportedForge f) {
		ForgeSearch search = null;
		switch (f) {
		case GitHub:
			search = SearchGitHub.getInstance();
			break;
		case SourceForge:
			search = SearchSourceForge.getInstance();
			break;
		case GoogleCode:
			search = SearchGoogleCode.getInstance();
			break;
		}
		return search;
	}
	
	public static ForgeCrawler defineForgeCrawler(SupportedForge f, File destinationFolder) {
		ForgeCrawler crawler = null;
		switch (f) {
		case GitHub:
			crawler = new CrawlGitHub(destinationFolder);
			break;
		case SourceForge:
			crawler = new CrawlSourceForge(destinationFolder); 
			break;
		case GoogleCode:
			crawler = new CrawlGoogleCode(destinationFolder);
			break;
		}
		return crawler;
	}
	
	public static CodeHistory defineCodeHistory(SCM scm) throws UnsupportedSCMException {
		CodeHistory codehistory = null;
		switch (scm) {
		case GIT:
			codehistory = GitCodeHistory.getInstance();
			break;
		case SOURCE_FORGE:
			codehistory = SFCodeHistory.getInstance(); 
			break;
		case SVN:
			codehistory = SvnCodeHistory.getInstance();
			break;
		default:
			throw new UnsupportedSCMException(scm);
		}
		return codehistory;
	}
	
	public static void freeResources(ForgeCrawler crawler) {
		crawler.shutdown();
		SVNClient.getInstance().close();
		Requests.getInstance().close();
	}
	
	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		Options opt = new Options();
		/*CmdLineParser cmd = new CmdLineParser(opt);
		try {
			cmd.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			cmd.printUsage(System.err);
			return;
		}*/
		opt.setDatetime("2012-01-01 12:00:00 GMT");
		//opt.setDestinationFolder(new File("download"));
		opt.setMetricsFolder(new File("metrics"));
		opt.setArguments(Arrays.asList("facebook", "api"));
		
		List<String> terms = opt.getArguments();
		String term = Joiner.on(" ").join(terms);
		File destinationFolder = opt.getDestinationFolder();
		boolean isDestinationTemp = false;
		if (destinationFolder == null) {
			destinationFolder = Files.createTempDir();
			isDestinationTemp = true;
		}
		File metricsFolder = opt.getMetricsFolder();
		if (!metricsFolder.exists()) {
			metricsFolder.mkdirs();
		}
		String datetimeStr = opt.getDatetime();
		Date datetime = null;
		try {
			datetime = dateFormat.parse(datetimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		int nProjects = opt.getnProjects();
		
		// Search for projects
		logger.info("Searching for projects...");
		ForgeSearch search = defineForgeSearch(opt.getForge());
		ForgeCrawler crawler = defineForgeCrawler(opt.getForge(), destinationFolder);
		List<ForgeProject> allProjects = null;
		List<ForgeProject> projects = new ArrayList<ForgeProject>();
		try {
			allProjects = search.getProjects(term, 1);
		} catch (SearchException e) {
			e.printStackTrace();
			return;
		}
		for (int i = 0; i < nProjects; i++) {
			projects.add(allProjects.get(i));
		}
		
		// Download projects
		logger.info("Downloading and processing projects...");
		List<Future<File>> futures = crawler.downloadProjects(projects);
		for (int i = 0; i < futures.size(); i++) {
			try {
				// Wait for project download
				ForgeProject project = projects.get(i);
				String name = project.getName();
				File repositoryFolder = futures.get(i).get();
				logger.info(f("Project %s was downloaded", name));
				
				// Checkout project to date
				logger.info(f("Checking out project %s to specified date...", name));
				CodeHistory codehistory = null;
				try {
					codehistory = defineCodeHistory(project.getSCM());
				} catch (UnsupportedSCMException e) {
					logger.warn(f("Project %s with unsupported SCM: %s", name, e.getSCM()));
					continue;
				}
				try {
					if (project.getSCM() != SCM.SVN) {
						codehistory.checkoutToDate(project.getName(), repositoryFolder, datetime);
					} else {
						codehistory.checkoutToDate(project.getName(), project.getScmURL(), datetime);
					}
				} catch (CheckoutException e) {
					e.printStackTrace();
					return;
				} catch (EmptyProjectAtDateException e) {
					logger.warn(f("Project %s was empty at specified date: %s", name, e.getDate()));
					continue;
				}
				logger.info(f("Project %s successfully checked out to %s", name, datetimeStr));
				
				// Parse project
				logger.info(f("Parsing project %s...", name));
				JavaParser parser = new JavaParser(repositoryFolder);
				JSONObject metrics = null;
				try {
					metrics = parser.parseToJSON();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
				
				// Save metrics to file
				logger.info(f("Project %s parsed, metrics extracted! Writing result to file %s...", name, name + ".json"));
				try {
					File metricsFile = new File(metricsFolder, name + ".json");
					FileUtils.writeStringToFile(metricsFile, metrics.toString());
					logger.info(f("Metrics of project %s written to file %s", name, metricsFile.getAbsolutePath()));
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			} catch (InterruptedException e) {
				logger.error(f("Failed to download project %s", projects.get(i).getName()));
			} catch (ExecutionException e) {
				logger.error(f("Failed to download project %s", projects.get(i).getName()));
			}
		}
		
		// Free resources and delete temp directory (if exists)
		freeResources(crawler);
		if (isDestinationTemp) {
			try {
				FileUtils.deleteDirectory(destinationFolder);
			} catch (IOException e) {
				logger.warn("Could not delete temp folders (but they will be eventually deleted)");
			}
		}
	}

}
