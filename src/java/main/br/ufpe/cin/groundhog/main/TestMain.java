package br.ufpe.cin.groundhog.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.codehistory.CodeHistoryModule;
import br.ufpe.cin.groundhog.codehistory.GitCodeHistory;
import br.ufpe.cin.groundhog.codehistory.SFCodeHistory;
import br.ufpe.cin.groundhog.codehistory.SvnCodeHistory;
import br.ufpe.cin.groundhog.crawler.CrawlGitHub;
import br.ufpe.cin.groundhog.crawler.CrawlGoogleCode;
import br.ufpe.cin.groundhog.crawler.CrawlSourceForge;
import br.ufpe.cin.groundhog.crawler.ForgeCrawler;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.http.Requests;
import br.ufpe.cin.groundhog.parser.JavaParser;
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
 * @author fjsj, gustavopinto, rodrigoalvesvieira
 */
public class TestMain {
	private static Logger logger = LoggerFactory.getLogger(TestMain.class);
	
	/**
	 * Test method for search the GitHub forge
	 * @param term the search term (project name)
	 * @throws Exception
	 */
	public static void gitHubExample(String term) throws Exception {
		File downloadFolder = FileUtil.getInstance().createTempDir();
		
		logger.info("1 - Search for projects according to term...");
		Injector injector = Guice.createInjector(new SearchModule(), new CodeHistoryModule(), new CodeHistoryModule(), new ScmModule());
		SearchGitHub search = injector.getInstance(SearchGitHub.class);
		
		List<Project> projects = search.getProjects(term, 1);
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		logger.info("2 - Download 1st result...");
		ForgeCrawler crawler = new CrawlGitHub(injector.getInstance(GitClient.class), downloadFolder);
		List<Future<File>> futures = crawler.downloadProjects(projects);
		crawler.shutdown();
		File repositoryFolder = null;
		for (Future<File> f : futures) { // wait for download
			repositoryFolder = f.get();
		}
		
		logger.info("3 - Checkout repository to a given date...");
		Date date = new GregorianCalendar(2012, 6, 1).getTime();
		GitCodeHistory codeHistory = injector.getInstance(GitCodeHistory.class);
		File temp = codeHistory.checkoutToDate(project.getName(), repositoryFolder, date);
		
		logger.info("4 - Parse...");
		JavaParser parser = new JavaParser(temp);
		String metrics = parser.format("JSON");
		System.out.println(metrics);
//		HashMap<String, HashMap<String, MutableInt>> counters = parser.parse();
//		JavaParser.printResult(counters);
		
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.info("Could not delete temp files :( (but they will be eventually deleted)");
		}
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
		
		List<Project> projects = search.getProjects("facebook chat", 1);
		if (projects.size() == 0) {
			logger.info("Ooops, no projects found! Aborting.");
			System.exit(0);
		}
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		logger.info("2 - Download 1st result...");
		Requests requests = injector.getInstance(Requests.class);
		
		ForgeCrawler crawler = new CrawlSourceForge(requests, downloadFolder);
		List<Future<File>> futures = crawler.downloadProjects(projects);
		crawler.shutdown();
		File repositoryFolder = null;
		for (Future<File> f : futures) { // wait for download
			repositoryFolder = f.get();
		}
		
		logger.info("3 - Checkout repository to a given date...");
		Date date = new GregorianCalendar(2012, 2, 21).getTime();
		SFCodeHistory codeHistory = injector.getInstance(SFCodeHistory.class);
		File temp = codeHistory.checkoutToDate(project.getName(), repositoryFolder, date);
		
		logger.info("4 - Parse...");
		JavaParser parser = new JavaParser(temp);
		String metrics = parser.format("CSV");
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
		
		List<Project> projects = search.getProjects(term, 1);
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		logger.info("2 - Download 1st result...");
		ForgeCrawler crawler = new CrawlGoogleCode(injector.getInstance(GitClient.class), downloadFolder);
		List<Future<File>> futures = crawler.downloadProjects(projects);
		crawler.shutdown();
		File repositoryFolder = null;
		for (Future<File> f : futures) { // wait for download
			repositoryFolder = f.get();
		}
		
		logger.info("3 - Checkout repository to a given date...");
		Date date = new GregorianCalendar(2011, 0, 2).getTime();
		File temp = null;
		if (project.getSCM() == SCM.SVN) {
			temp = injector.getInstance(SvnCodeHistory.class).checkoutToDate(project.getName(), project.getScmURL(), date);
		} else if (project.getSCM() == SCM.GIT) {
			temp = injector.getInstance(GitCodeHistory.class).checkoutToDate(project.getName(), repositoryFolder, date);
		} else {
			logger.error("Can't continue with parsing step. Unwkown SCM.");
			System.exit(0);
		}
		
		logger.info("4 - Parse...");
		JavaParser parser = new JavaParser(temp);
		String metrics = parser.format("CSV");
		System.out.println(metrics);
		
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.info("Could not delete temp files :( (but they will be eventually deleted)");
		}
	}
	
	public static void main(String[] args) throws Exception {
		gitHubExample("jsoup");
//		sourceForgeExample();
//		googleCodeExample("facebook-java-api"); // Google Code SVN
//		googleCodeExample("guava-libraries"); // Google Code Git
	}
}