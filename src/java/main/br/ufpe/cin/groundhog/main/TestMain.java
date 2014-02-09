package br.ufpe.cin.groundhog.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.Commit;
import br.ufpe.cin.groundhog.Issue;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.codehistory.CodeHistoryModule;
import br.ufpe.cin.groundhog.codehistory.GitCodeHistory;
import br.ufpe.cin.groundhog.codehistory.SFCodeHistory;
import br.ufpe.cin.groundhog.crawler.CrawlGitHub;
import br.ufpe.cin.groundhog.crawler.CrawlGoogleCode;
import br.ufpe.cin.groundhog.crawler.CrawlSourceForge;
import br.ufpe.cin.groundhog.crawler.ForgeCrawler;
import br.ufpe.cin.groundhog.database.GroundhogDB;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.http.Requests;
import br.ufpe.cin.groundhog.parser.java.JavaParser;
import br.ufpe.cin.groundhog.parser.java.MutableInt;
import br.ufpe.cin.groundhog.parser.java.formater.CSVFormater;
import br.ufpe.cin.groundhog.parser.java.formater.FormaterFactory;
import br.ufpe.cin.groundhog.parser.java.formater.JSONFormater;
import br.ufpe.cin.groundhog.scmclient.GitClient;
import br.ufpe.cin.groundhog.scmclient.ScmModule;
import br.ufpe.cin.groundhog.search.SearchGitHub;
import br.ufpe.cin.groundhog.search.SearchGoogleCode;
import br.ufpe.cin.groundhog.search.SearchModule;
import br.ufpe.cin.groundhog.search.SearchSourceForge;
import br.ufpe.cin.groundhog.util.FileUtil;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The main test class
 * @author fjsj, gustavopinto, Rodrigo Alves
 * @since 0.0.1
 */
public class TestMain {
	private static Logger logger = LoggerFactory.getLogger(TestMain.class);
	static Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
	static SearchGitHub searchGitHub = injector.getInstance(SearchGitHub.class);

	/**
	 * Test method for search the GitHub forge
	 * @param term the search term (project name)
	 * @throws Exception
	 */
	public static void gitHubExample(String term) throws Exception {
		File downloadFolder = FileUtil.getInstance().createTempDir();
		
		//1 - Search for projects according to term...
		Injector injector = Guice.createInjector(new SearchModule(), new CodeHistoryModule(), new CodeHistoryModule(), new ScmModule(), new HttpModule());
		SearchGitHub search = injector.getInstance(SearchGitHub.class);
		
		List<Project> projects = search.getProjects(term, 1, 2);
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		//2 - Download 1st result...
		ForgeCrawler crawler = new CrawlGitHub(injector.getInstance(GitClient.class), downloadFolder);
		File repositoryFolder = crawler.downloadProject(projects.get(0));
		
		//3 - Checkout repository to a given date...
		Date date = new GregorianCalendar(2012, 07, 10).getTime();
		GitCodeHistory codeHistory = injector.getInstance(GitCodeHistory.class);
		File temp = codeHistory.checkoutToDate(project.getName(), repositoryFolder, date);

		//4 - Parse...
		HashMap<String, HashMap<String, MutableInt>> javaMetrics = new JavaParser(temp).parser();
		String metrics = FormaterFactory.get(JSONFormater.class).format(javaMetrics);
		System.out.println(metrics);
				
		try {
			FileUtil.getInstance().deleteTempDirs();
			logger.info("Cleaning up the garbage..");
		} catch (IOException e) {
			logger.error("Could not delete temp files :( (but they will be eventually deleted)");
		}
		
		logger.info("That is it! Groundhog is done!");
	}
	
	/**
	 * Test method for search the SourceForge forge
	 * @throws Exception
	 */
	public static void sourceForgeExample() throws Exception {
		File downloadFolder = FileUtil.getInstance().createTempDir();
		
		logger.info("1 - Search for projects according to term...");
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule(), new CodeHistoryModule());
		SearchSourceForge search = injector.getInstance(SearchSourceForge.class);
		
		List<Project> projects = search.getProjects("facebook chat", 1, -1);
		if (projects.size() == 0) {
			logger.info("Ooops, no projects found! Aborting.");
			System.exit(0);
		}
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		logger.info("2 - Download 1st result...");
		Requests requests = injector.getInstance(Requests.class);
		
		ForgeCrawler crawler = new CrawlSourceForge(requests, downloadFolder);
		List<Future<File>> futures = crawler.asyncDownloadProjects(projects);
		File repositoryFolder = null;
		for (Future<File> f : futures) { // wait for download
			repositoryFolder = f.get();
		}
		
		logger.info("3 - Checkout repository to a given date...");
		Date date = new GregorianCalendar(2012, 2, 21).getTime();
		SFCodeHistory codeHistory = injector.getInstance(SFCodeHistory.class);
		File temp = codeHistory.checkoutToDate(project.getName(), repositoryFolder, date);
		
		logger.info("4 - Parse...");
		HashMap<String, HashMap<String, MutableInt>> javaMetrics = new JavaParser(temp).parser();
		String metrics = FormaterFactory.get(CSVFormater.class).format(javaMetrics);
		System.out.println(metrics);
		
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.info("Could not delete temp files :( (but they will be eventually deleted)");
		}
	}
	
	/**
	 * Test method for search the Google Code forge
	 * @param term the search term (project name)
	 * @throws Exception
	 */
	public static void googleCodeExample(String term) throws Exception {
		File downloadFolder = FileUtil.getInstance().createTempDir();
		
		logger.info("1 - Search for projects according to term...");
		Injector injector = Guice.createInjector(new SearchModule(), new CodeHistoryModule(), new ScmModule());
		SearchGoogleCode search = injector.getInstance(SearchGoogleCode.class);
		
		List<Project> projects = search.getProjects(term, 1, -1);
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		logger.info("2 - Download 1st result...");
		ForgeCrawler crawler = new CrawlGoogleCode(injector.getInstance(GitClient.class), downloadFolder);
		List<Future<File>> futures = crawler.asyncDownloadProjects(projects);
		File repositoryFolder = null;
		for (Future<File> f : futures) { // wait for download
			repositoryFolder = f.get();
		}
		
		logger.info("3 - Checkout repository to a given date...");
		Date date = new GregorianCalendar(2011, 0, 2).getTime();
		File temp = null;
		if (project.getSCM() == SCM.GIT) {
			temp = injector.getInstance(GitCodeHistory.class).checkoutToDate(project.getName(), repositoryFolder, date);
		} else {
			logger.error("Can't continue with parsing step. Unwkown SCM.");
			System.exit(0);
		}
		
		logger.info("4 - Parse...");
		HashMap<String, HashMap<String, MutableInt>> javaMetrics = new JavaParser(temp).parser();
		String metrics = FormaterFactory.get(CSVFormater.class).format(javaMetrics);
		System.out.println(metrics);
		
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.info("Could not delete temp files :( (but they will be eventually deleted)");
		}
	}
	
	public static void main(String[] args) throws Exception {
//		gitHubExample("restfulie-java");
		
		Injector injector = Guice.createInjector(new SearchModule());
	    SearchGitHub searchGitHub = injector.getInstance(SearchGitHub.class);
//	    List<Project> projects = searchGitHub.getProjects("github", "android", 1);
	    
	    GroundhogDB db = new GroundhogDB("127.0.0.1", "myGitHubResearch");
	    
	    Project project = new Project("rails", "rails");
	    
	    // Fetches all commits of the project and persists each one of them to the database
	    List<Commit> commits = searchGitHub.getAllProjectCommits(project);
	    
	    for (Commit comm: commits) {
	    	db.save(comm);
	    	System.out.println(comm);
	    }
	    
//	    Project githubAndroidApp = projects.get(0);
//	    githubAndroidApp.setIssues(searchGitHub.getAllProjectIssues(githubAndroidApp));
//	    
//	    List<Issue> issues = githubAndroidApp.getIssues();
//	    
//	    System.out.println("Issues for project: " + githubAndroidApp.getName());
//
//	    for (Issue issue : issues) {
//	    	System.out.println(issue);
//	    }
	}
}