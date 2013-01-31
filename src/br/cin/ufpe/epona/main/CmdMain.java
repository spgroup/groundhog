package br.cin.ufpe.epona.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.ufpe.epona.Config;
import br.cin.ufpe.epona.Project;
import br.cin.ufpe.epona.SCM;
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
import br.cin.ufpe.epona.http.Requests;
import br.cin.ufpe.epona.parser.JavaParser;
import br.cin.ufpe.epona.scmclient.EmptyProjectAtDateException;
import br.cin.ufpe.epona.scmclient.SVNClient;
import br.cin.ufpe.epona.search.ForgeSearch;
import br.cin.ufpe.epona.search.SearchException;
import br.cin.ufpe.epona.search.SearchGitHub;
import br.cin.ufpe.epona.search.SearchGoogleCode;
import br.cin.ufpe.epona.search.SearchSourceForge;
import br.cin.ufpe.epona.util.FileUtil;

import com.google.common.base.Joiner;

public class CmdMain {

	private static Logger logger = LoggerFactory.getLogger(CrawlGoogleCode.class);
	
	private static String f(String s, Object... args) {
		return String.format(s, args);
	}
	
	public static ForgeSearch defineForgeSearch(SupportedForge f) {
		ForgeSearch search = null;
		switch (f) {
		case GITHUB:
			search = SearchGitHub.getInstance();
			break;
		case SOURCEFORGE:
			search = SearchSourceForge.getInstance();
			break;
		case GOOGLECODE:
			search = SearchGoogleCode.getInstance();
			break;
		}
		return search;
	}
	
	public static ForgeCrawler defineForgeCrawler(SupportedForge f, File destinationFolder) {
		ForgeCrawler crawler = null;
		switch (f) {
		case GITHUB:
			crawler = new CrawlGitHub(destinationFolder);
			break;
		case SOURCEFORGE:
			crawler = new CrawlSourceForge(destinationFolder); 
			break;
		case GOOGLECODE:
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
			throw new UnsupportedSCMException(scm.toString());
		}
		return codehistory;
	}
	
	public static File downloadAndCheckoutProject(Project project, Date datetime, Future<File> repositoryFolderFuture)
			throws InterruptedException, ExecutionException, CheckoutException {
		// Wait for project download
		String name = project.getName();
		File repositoryFolder = repositoryFolderFuture.get();
		logger.info(f("Project %s was downloaded", name));
		
		// Checkout project to date
		String datetimeStr = Options.getDateFormat().format(datetime);
		logger.info(f("Checking out project %s to %s...", name, datetimeStr));
		CodeHistory codehistory = null;
		try {
			codehistory = defineCodeHistory(project.getSCM());
		} catch (UnsupportedSCMException e) {
			logger.warn(f("Project %s has an unsupported SCM: %s", name, e.getMessage()));
			return null;
		}
		File checkedOutRepository = null;
		try {
			if (project.getSCM() != SCM.SVN) {
				checkedOutRepository = codehistory.checkoutToDate(project.getName(), repositoryFolder, datetime);
			} else {
				checkedOutRepository = codehistory.checkoutToDate(project.getName(), project.getScmURL(), datetime);
			}
		} catch (EmptyProjectAtDateException e) {
			logger.warn(f("Project %s was empty at specified date: %s", name, datetimeStr));
			return null;
		}
		logger.info(f("Project %s successfully checked out to %s", name, datetimeStr));	
		
		return checkedOutRepository;
	}
	
	public static void analyzeProject(Project project, File projectFolder, Date datetime, File metricsFolder)
			throws IOException, JSONException {
		String name = project.getName();
		String datetimeStr = Options.getDateFormat().format(datetime);
		
		// Parse project
		logger.info(f("Parsing project %s...", name));
		JavaParser parser = new JavaParser(projectFolder);
		JSONObject metrics = null;
		metrics = parser.parseToJSON();
		
		if (metrics != null) {
			// Save metrics to file
			String metricsFilename = f("%s-%s.json", name, datetimeStr);
			logger.info(f("Project %s parsed, metrics extracted! Writing result to file %s...", name, metricsFilename));
			File metricsFile = new File(metricsFolder, metricsFilename);
			FileUtil.getInstance().writeStringToFile(metricsFile, metrics.toString());
			logger.info(f("Metrics of project %s written to file %s", name, metricsFile.getAbsolutePath()));
		} else {
			logger.warn(f("Project %s has no Java source files! Metrics couldn't be extracted...", name));
		}
	}
	
	public static void freeResources(ForgeCrawler crawler, OutputStream errorStream) {
		crawler.shutdown();
		SVNClient.getInstance().close();
		Requests.getInstance().close();
		try {
			FileUtil.getInstance().deleteTempDirs();
		} catch (IOException e) {
			logger.warn("Could not delete temp folders (but they will be eventually deleted)");
		}
		try {
			if (errorStream != null) {
				errorStream.close();
			}
		} catch (IOException e) {
			logger.error("Unable to close error.log stream", e);
		}
	}
	
	public static void main(String[] args) {
		Options opt = new Options();
		/*CmdLineParser cmd = new CmdLineParser(opt);
		try {
			cmd.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			cmd.printUsage(System.err);
			return;
		}*/
		opt.setDatetime("2012-07-01_12_00");
		//opt.setDestinationFolder(new File("download"));
		opt.setForge(SupportedForge.GITHUB);
		opt.setMetricsFolder(new File("metrics"));
		opt.setnProjects(5);
		opt.setArguments(Arrays.asList("facebook"));
		
		List<String> terms = opt.getArguments();
		String term = Joiner.on(" ").join(terms);
		File destinationFolder = opt.getDestinationFolder();
		if (destinationFolder == null) {
			destinationFolder = FileUtil.getInstance().createTempDir();
		} else {
			if (destinationFolder.list().length > 0) {
				logger.warn("Attention, destination folder isn't empty! " +
						"Errors may happen if any of the projects to be downloaded already exist in this folder.");
			}
		}
		File metricsFolder = opt.getMetricsFolder();
		if (!metricsFolder.exists()) {
			metricsFolder.mkdirs();
		}
		Date datetime = null;
		try {
			datetime = opt.getDatetime();
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		int nProjects = opt.getnProjects();
		
		// Redirect System.err to file
		PrintStream errorStream = null;
		try {
			errorStream = new PrintStream("error.log");
			System.setErr(errorStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Set ThreadsConfig.nThreads
		Config.MAX_NUMBER_OF_THREADS = opt.getnThreads();
		
		// Search for projects
		logger.info("Searching for projects...");
		ForgeSearch search = defineForgeSearch(opt.getForge());
		ForgeCrawler crawler = defineForgeCrawler(opt.getForge(), destinationFolder);
		List<Project> allProjects = null;
		List<Project> projects = new ArrayList<Project>();
		try {
			allProjects = search.getProjects(term, 1);
		} catch (SearchException e) {
			e.printStackTrace();
			return;
		}
		for (int i = 0; i < nProjects; i++) {
			projects.add(allProjects.get(i));
		}
		
		// Download and analyze projects
		logger.info("Downloading and processing projects...");
		ExecutorService ex = Executors.newFixedThreadPool(Config.MAX_NUMBER_OF_THREADS);
		List<Future<File>> downloadFutures = crawler.downloadProjects(projects);
		List<Future<?>> analysisFutures = new ArrayList<Future<?>>();
		for (int i = 0; i < downloadFutures.size(); i++) {
			final Project project = projects.get(i);
			final Date datetime_ = datetime;
			final Future<File> repositoryFolderFuture = downloadFutures.get(i);
			final File metricsFolder_ = metricsFolder;
			
			analysisFutures.add(ex.submit(new Runnable() {
				@Override
				public void run() { 
					File checkedOutRepository = null;
					try {
						checkedOutRepository = downloadAndCheckoutProject(project, datetime_, repositoryFolderFuture);
					} catch (Exception e) {
						logger.error(f("Error while downloading project %s", project.getName()), e);
					}
					if (checkedOutRepository != null) {
						try {
							analyzeProject(project, checkedOutRepository, datetime_, metricsFolder_);
						} catch (Exception e) {
							logger.error(f("Error while analyzing project %s", project.getName()), e);
						}
					} else {
						logger.warn(f("Project %s can't be analyzed", project.getName()));
					}
				}
			}));
		}
		ex.shutdown();
		for (int i = 0; i < analysisFutures.size(); i++) {
			try {
				analysisFutures.get(i).get();
			} catch (InterruptedException e) {
				logger.error(f("Error while analyzing project %s", projects.get(i).getName()), e);
			} catch (ExecutionException e) {
				logger.error(f("Error while analyzing project %s", projects.get(i).getName()), e);
			}
		}
		
		// Free resources and delete temp directories
		logger.info("Disposing resources...");
		freeResources(crawler, errorStream);
		logger.info("Done!");
	}

}
