package br.cin.ufpe.groundhog.main;

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

import br.cin.ufpe.groundhog.Project;
import br.cin.ufpe.groundhog.SCM;
import br.cin.ufpe.groundhog.codehistory.GitCodeHistory;
import br.cin.ufpe.groundhog.codehistory.SFCodeHistory;
import br.cin.ufpe.groundhog.codehistory.SvnCodeHistory;
import br.cin.ufpe.groundhog.crawler.CrawlGitHub;
import br.cin.ufpe.groundhog.crawler.CrawlGoogleCode;
import br.cin.ufpe.groundhog.crawler.CrawlSourceForge;
import br.cin.ufpe.groundhog.crawler.ForgeCrawler;
import br.cin.ufpe.groundhog.http.Requests;
import br.cin.ufpe.groundhog.parser.JavaParser;
import br.cin.ufpe.groundhog.parser.MutableInt;
import br.cin.ufpe.groundhog.search.SearchGitHub;
import br.cin.ufpe.groundhog.search.SearchGoogleCode;
import br.cin.ufpe.groundhog.search.SearchSourceForge;
import br.cin.ufpe.groundhog.util.FileUtil;

public class TestMain {
	private static Logger logger = LoggerFactory.getLogger(TestMain.class);
	
	public static void gitHubExample(String term) throws Exception {
		File downloadFolder = FileUtil.getInstance().createTempDir();
		
		logger.info("1 - Search for projects according to term...");
		List<Project> projects = SearchGitHub.getInstance().getProjects(term, 1);
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		logger.info("2 - Download 1st result...");
		ForgeCrawler crawler = new CrawlGitHub(downloadFolder);
		List<Future<File>> futures = crawler.downloadProjects(projects);
		crawler.shutdown();
		File repositoryFolder = null;
		for (Future<File> f : futures) { // wait for download
			repositoryFolder = f.get();
		}
		
		logger.info("3 - Checkout repository to a given date...");
		Date date = new GregorianCalendar(2012, 6, 1).getTime();
		File temp = GitCodeHistory.getInstance().checkoutToDate(project.getName(), repositoryFolder, date);
		
		logger.info("4 - Parse...");
		JavaParser parser = new JavaParser(temp);
		HashMap<String, HashMap<String, MutableInt>> counters = parser.parse();
		JavaParser.printResult(counters);
		
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.info("Could not delete temp files :( (but they will be eventually deleted)");
		}
	}
	
	public static void sourceForgeExample() throws Exception {
		File downloadFolder = FileUtil.getInstance().createTempDir();
		
		logger.info("1 - Search for projects according to term...");
		List<Project> projects = SearchSourceForge.getInstance().getProjects("facebook chat", 1);
		if (projects.size() == 0) {
			logger.info("Ooops, no projects found! Aborting.");
			System.exit(0);
		}
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		logger.info("2 - Download 1st result...");
		ForgeCrawler crawler = new CrawlSourceForge(downloadFolder);
		List<Future<File>> futures = crawler.downloadProjects(projects);
		crawler.shutdown();
		File repositoryFolder = null;
		for (Future<File> f : futures) { // wait for download
			repositoryFolder = f.get();
		}
		
		logger.info("3 - Checkout repository to a given date...");
		Date date = new GregorianCalendar(2012, 2, 21).getTime();
		File temp = SFCodeHistory.getInstance().checkoutToDate(project.getName(), repositoryFolder, date);
		
		logger.info("4 - Parse...");
		JavaParser parser = new JavaParser(temp);
		HashMap<String, HashMap<String, MutableInt>> counters = parser.parse();
		JavaParser.printResult(counters);
		
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.info("Could not delete temp files :( (but they will be eventually deleted)");
		}
	}
	
	public static void googleCodeExample(String term) throws Exception {
		File downloadFolder = FileUtil.getInstance().createTempDir();
		
		logger.info("1 - Search for projects according to term...");
		List<Project> projects = SearchGoogleCode.getInstance().getProjects(term, 1);
		Project project = projects.get(0);
		projects = Arrays.asList(project); // analyze only the first project 
		
		logger.info("2 - Download 1st result...");
		ForgeCrawler crawler = new CrawlGoogleCode(downloadFolder);
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
			temp = SvnCodeHistory.getInstance().checkoutToDate(project.getName(), project.getScmURL(), date);
		} else if (project.getSCM() == SCM.GIT) {
			temp = GitCodeHistory.getInstance().checkoutToDate(project.getName(), repositoryFolder, date);
		} else {
			logger.error("Can't continue with parsing step. Unwkown SCM.");
			System.exit(0);
		}
		
		logger.info("4 - Parse...");
		JavaParser parser = new JavaParser(temp);
		HashMap<String, HashMap<String, MutableInt>> counters = parser.parse();
		JavaParser.printResult(counters);
		
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.info("Could not delete temp files :( (but they will be eventually deleted)");
		}
	}
	
	public static void main(String[] args) throws Exception {
		//gitHubExample("jsoup");
		//sourceForgeExample();
		googleCodeExample("facebook-java-api"); // Google Code SVN
		//googleCodeExample("guava-libraries"); // Google Code Git
		Requests.getInstance().close();
	}
}