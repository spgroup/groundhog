package br.ufpe.cin.groundhog.main;

import static java.lang.String.format;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufpe.cin.groundhog.GroundhogException;
import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.codehistory.CodeHistory;
import br.ufpe.cin.groundhog.codehistory.CodeHistoryModule;
import br.ufpe.cin.groundhog.codehistory.GitCodeHistory;
import br.ufpe.cin.groundhog.codehistory.SFCodeHistory;
import br.ufpe.cin.groundhog.codehistory.SvnCodeHistory;
import br.ufpe.cin.groundhog.codehistory.UnsupportedForgeException;
import br.ufpe.cin.groundhog.crawler.CrawlGitHub;
import br.ufpe.cin.groundhog.crawler.CrawlGoogleCode;
import br.ufpe.cin.groundhog.crawler.CrawlSourceForge;
import br.ufpe.cin.groundhog.crawler.ForgeCrawler;
import br.ufpe.cin.groundhog.database.GroundhogDB;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.http.Requests;
import br.ufpe.cin.groundhog.metrics.JavaProject;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaFileException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidJavaProjectPathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidSourceRootCodePathException;
import br.ufpe.cin.groundhog.metrics.exception.InvalidTestSourcePathException;
import br.ufpe.cin.groundhog.parser.java.JavaParser;
import br.ufpe.cin.groundhog.parser.java.MutableInt;
import br.ufpe.cin.groundhog.parser.java.NotAJavaProjectException;
import br.ufpe.cin.groundhog.parser.java.formater.CSVFormater;
import br.ufpe.cin.groundhog.parser.java.formater.Formater;
import br.ufpe.cin.groundhog.parser.java.formater.FormaterFactory;
import br.ufpe.cin.groundhog.scmclient.EmptyProjectAtDateException;
import br.ufpe.cin.groundhog.scmclient.GitClient;
import br.ufpe.cin.groundhog.scmclient.ScmModule;
import br.ufpe.cin.groundhog.search.ForgeSearch;
import br.ufpe.cin.groundhog.search.SearchGitHub;
import br.ufpe.cin.groundhog.search.SearchGoogleCode;
import br.ufpe.cin.groundhog.search.SearchModule;
import br.ufpe.cin.groundhog.search.SearchSourceForge;
import br.ufpe.cin.groundhog.util.Dates;
import br.ufpe.cin.groundhog.util.FileUtil;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The groundhog entry point
 * 
 * @author fjsj, gustavopinto
 * @since 0.0.1
 */

public final class CmdMain extends GroundhogMain {
	private static Logger logger = LoggerFactory.getLogger(CmdMain.class);

	/**
	 * Defines the code forge where the search will be performed. Valid options
	 * are GITHUB, SOURCEFORGE and GOOGLECODE, as listed in the
	 * {@link SupportedForge} enumerator.
	 * 
	 * @param f
	 *            the forge name
	 * @return the search object of the chosen forge
	 */
	public ForgeSearch defineForgeSearch(SupportedForge f) {
		Injector injector = Guice.createInjector(new SearchModule());
		ForgeSearch search = null;
		switch (f) {
		case GITHUB:
			search = injector.getInstance(SearchGitHub.class);
			break;
		case SOURCEFORGE:
			search = injector.getInstance(SearchSourceForge.class);
			break;
		case GOOGLECODE:
			search = injector.getInstance(SearchGoogleCode.class);
			break;
		}
		return search;
	}

	/**
	 * Defines the forge crawler - that is - how the search will actually be
	 * performed on the chosen forge
	 * 
	 * @param f
	 *            the supported forge. Valid options are GITHUB, SOURCEFORGE and
	 *            GOOGLECODE
	 * @param destinationFolder
	 *            the destination directory
	 * @return the {@link ForgeCrawler} object for the chosen forge
	 */
	public ForgeCrawler defineForgeCrawler(SupportedForge f,
			File destinationFolder) {
		ForgeCrawler crawler = null;
		Injector injector = Guice.createInjector(new HttpModule(),
				new ScmModule());
		switch (f) {
		case GITHUB:
			GitClient client = injector.getInstance(GitClient.class);
			crawler = new CrawlGitHub(client, destinationFolder);
			break;
		case SOURCEFORGE:
			Requests requests = injector.getInstance(Requests.class);
			crawler = new CrawlSourceForge(requests, destinationFolder);
			break;
		case GOOGLECODE:
			GitClient gitClient = injector.getInstance(GitClient.class);
			crawler = new CrawlGoogleCode(gitClient, destinationFolder);
			break;
		}
		return crawler;
	}

	/**
	 * Defines the code history analysis mechanism according to the way it can
	 * be done for the searched projects. Git, SourceForge or SVN.
	 * 
	 * @param scm
	 * @return a {@link CodeHistory} object
	 * @throws UnsupportedForgeException
	 *             throw if the given SCM mechanism is not supported by
	 *             Groundhog
	 */
	public CodeHistory defineCodeHistory(SCM scm)
			throws UnsupportedForgeException {
		Injector injector = Guice.createInjector(new CodeHistoryModule());
		CodeHistory codehistory = null;
		switch (scm) {
		case GIT:
			codehistory = injector.getInstance(GitCodeHistory.class);
			break;
		case SOURCE_FORGE:
			codehistory = injector.getInstance(SFCodeHistory.class);
			break;
		case SVN:
			codehistory = injector.getInstance(SvnCodeHistory.class);
			break;
		default:
			throw new UnsupportedForgeException(scm.toString());
		}
		return codehistory;
	}

	/**
	 * Performs the download and checkout of the given project
	 * 
	 * @param project
	 *            the project to be downloaded and have its source code checked
	 *            out
	 * @param datetime
	 *            the informed {@link Datetime}
	 * @param repositoryFolderFuture
	 * @return the checked out repository
	 */
	public File downloadAndCheckoutProject(Project project,
			Date datetime, Future<File> repositoryFolderFuture) throws EmptyProjectAtDateException {

		String name = project.getName();
		String datetimeStr = new Dates("yyyy-MM-dd").format(datetime);

		try {
			// Wait for project download

			File repositoryFolder = repositoryFolderFuture.get();
			logger.info(format("Project %s was downloaded", name));

			logger.info(format("Project has %d forks", project.getForksCount()));

			logger.info(format("Checking out project %s to %s...", name, datetimeStr));
			CodeHistory codehistory = defineCodeHistory(project.getSCM());

			File checkedOutRepository = null;

			if (project.getSCM() == SCM.GIT) {
				checkedOutRepository = codehistory.checkoutToDate(project.getName(), repositoryFolder, datetime);
			}
			logger.info(format("Project %s successfully checked out to %s", name, datetimeStr));

			return checkedOutRepository;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(format("Error while downloading project %s: %s", name, e.getMessage()));
			logger.error(format("Project %s can't be analyzed", project.getName()));
			return null;
		}
	}

	/**
	 * Analyzes the project's source code via a {@link JavaParser} and parses
	 * the result into JSON format
	 * 
	 * @param project
	 *            the project to be analyzed
	 * @param projectFolder
	 *            the project folder where the source code to be analyzed is
	 *            located
	 * @param datetime
	 * @param metricsFolder
	 *            the directory where the JSON metrics output will be stored
	 */
	public void analyzeProject(Project project, File projectFolder,
			Date datetime, File metricsFolder, Formater metricsFormat) {
		String name = project.getName();
		String datetimeStr = new Dates("yyyy-MM-dd").format(datetime);

		try {
			// Parse project
			logger.info(format("Parsing project %s...", name));
			HashMap<String, HashMap<String, MutableInt>> javaMetrics = new JavaParser(projectFolder).parser();
			String metrics = FormaterFactory.get(CSVFormater.class).format(javaMetrics);

			// Save metrics to file
			String metricsFilename = format("%s-%s.%s", name, datetimeStr, metricsFormat.toString());
			logger.info(format("Project %s parsed and metrics extracted! Writing results to file %s...", name, metricsFilename));

			File metricsFile = new File(metricsFolder, metricsFilename);
			FileUtil.getInstance().writeStringToFile(metricsFile, metrics);

			logger.info(format("Metrics of project %s written to file %s", name, metricsFile.getAbsolutePath()));
		} catch (NotAJavaProjectException e) {
			logger.warn(format(e.getMessage(), name));
		} catch (Exception e) {
			logger.error(format("Error while analyzing project %s:%s", project.getName()), e.getMessage());
		}
	}


	@Override
	public void run(JsonInputFile input) {
		try {

			//Java Metrics
			if(input.getDBName()!= null || input.getJavaProjectSourceRootPath() != null || input.getJavaProjectSourceRootTestPath() != null){
				
				final GroundhogDB ghdb = new GroundhogDB("127.0.0.1", input.getDBName());
				ghdb.getMapper().mapPackage("br.ufpe.cin.groundhog.metrics");
				
				JavaProject project = new JavaProject(input.getJavaProjectPath());
				try {
					project.generateStructure(input.getJavaProjectSourceRootPath(), input.getJavaProjectSourceRootTestPath());
				} catch (Exception e) {
					logger.error("The struture of passed Java project can't be reconstructed!\nPlease check your parameters");
					e.printStackTrace();
				}
				
				project.generateMetrics(
						input.getJavaProjectSourceRootTestPath() != null ? true: false,
								ghdb);

			}else{
				
				final File destinationFolder = input.getDest();
				final File metricsFolder = input.getOut();

				logger.info("Creating temp folders...");
				createTempFolders(destinationFolder, metricsFolder);

				final Date datetime = input.getDatetime();
				final int nProjects = input.getNprojects();
				final String username = input.getSearch().getUsername();

				// Search for projects
				logger.info("Searching for projects... " + input.getSearch().getProjects());
				ForgeSearch search = defineForgeSearch(input.getForge());
				ForgeCrawler crawler = defineForgeCrawler(input.getForge(), destinationFolder);

				String term = input.getSearch().getProjects().get(0);

				List<Project> allProjects = null;
				if(username != null && !username.isEmpty()) {
					allProjects = search.getProjects(term, username, 1);				
				} else {
					allProjects = search.getProjects(term, 1,-1);
				}

				//TODO the getProjects method already limits the number of searched projects
				List<Project> projects = new ArrayList<Project>();
				for (int i = 0; i < nProjects; i++) {
					if (i < allProjects.size()) {
						projects.add(allProjects.get(i));
					}
				}

				// Download and analyze projects
				logger.info("Downloading and processing projects...");
				ExecutorService ex = Executors.newFixedThreadPool(JsonInputFile.getMaxThreads());
				List<Future<File>> downloadFutures = crawler.asyncDownloadProjects(projects);
				List<Future<?>> analysisFutures = new ArrayList<Future<?>>();

				for (int i = 0; i < downloadFutures.size(); i++) {
					final Project project = projects.get(i);
					final Future<File> repositoryFolderFuture = downloadFutures.get(i);
					final Formater metricsFormat = input.getOutputformat();

					analysisFutures.add(ex.submit(new Runnable() {
						@Override
						public void run() {
							File checkedOutRepository = downloadAndCheckoutProject(project, datetime, repositoryFolderFuture);

							if (checkedOutRepository != null) {
								analyzeProject(project, checkedOutRepository, datetime, metricsFolder, metricsFormat);
							}
						}
					}));
				}

				ex.shutdown();
				for (int i = 0; i < analysisFutures.size(); i++) {
					try {
						analysisFutures.get(i).get();
					} catch (InterruptedException | ExecutionException e) {
						logger.error(format("Error while analyzing project %s", projects.get(i).getName()), e);
					}
				}
				logger.info("All projects were downloaded and analyzed!");
			}
			
		} catch (GroundhogException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			logger.error("Fail to acess MongoDB database, please check if its installed and running");
		}
	}

	private void createTempFolders(File... folders) {
		for (File file : folders) {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}
}