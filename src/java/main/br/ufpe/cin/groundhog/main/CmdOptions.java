package br.ufpe.cin.groundhog.main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import br.ufpe.cin.groundhog.GroundhogException;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

enum SupportedForge {
	GITHUB, SOURCEFORGE, GOOGLECODE
}

/**
 * The command-line options parsing class
 * 
 * @author fjsj, gustavopinto, Rodrigo Alves
 */
public class CmdOptions {

	@Option(name = "-forge", usage = "forge to be used in search and crawling process")
	private SupportedForge forge = SupportedForge.GITHUB;

	@Option(name = "-dest", usage = "destination folder into which projects will be downloaded")
	private File destinationFolder = new File("projects");

	@Option(name = "-out", usage = "output folder to metrics files")
	private File metricsFolder = new File("metrics");

	@Option(name = "-datetime", usage = "datetime of projects source code to be processed")
	private String datetime = "2012-10-01 12:00";

	@Option(name = "-nprojects", usage = "maximum number of projects to be downloaded and processed")
	private int nProjects = 4;

	@Option(name = "-format", usage = "determine the output format of the metrics")
	private String metricsFormat = "csv";
	
	@Option(name = "-user", usage = "determine the output format of the metrics")
	private String username = "";

	@Option(name = "-githubtoken", usage = "use authenticated requests to github api")
	private String gitHubOauthAcessToken;
	
	@Option(name = "-jm", usage = "use to extract some metrics from java projects, like McGabe complexity and total lines of code (TLOC), to the project passed\n"
			+ "By default the path is your actual directory",
			aliases = {"--generate-java-metrics"}, metaVar = "<file>")
	private File projectPath = new File(System.getProperty("user.dir"));
	
	@Option(name = "-src", usage = "use this option to provide the path of source root code directory from project root",
			aliases = {"--source-root-code"}, metaVar = "<file>")
	private File child_path_src;
	
	@Option(name = "-srtc", usage = "use this option to provide the path of source root code directory from project root",
			aliases = {"--source-root-test-code"}, metaVar = "<file>")
	private File child_path_srtc;
	
	@Option(name = "-dm", usage = "use this option to provide the name of Groundhog database when the generated metrics will be stored.\n"
			+ "By default the database name are java_metrics",
			aliases = {"--database-name"})
	private String dbName = "java_metrics";
	
	
	
	private JsonInputFile input = null;

	@Argument
	private List<String> arguments = new ArrayList<String>();

	/**
	 * Informs the code forge where the project search will be performed
	 * 
	 * @return
	 */
	public SupportedForge getForge() {
		return this.forge;
	}

	/**
	 * Returns the destination folder into which projects will be downloaded
	 * 
	 * @return
	 */
	public File getDestinationFolder() {
		return this.destinationFolder;
	}

	/**
	 * Sets the destination folder into which projects will be downloaded
	 * 
	 * @param destinationFolder
	 */
	public void setDestinationFolder(File destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

	/**
	 * Informs the location of the folder where the metrics will be stored
	 * 
	 * @return A {@link File} object correspondent to the metrics folder
	 */
	public File getMetricsFolder() {
		return this.metricsFolder;
	}

	/**
	 * Sets the location of the folder where the metrics will be stored
	 * 
	 * @param metricsFolder
	 */
	public void setMetricsFolder(File metricsFolder) {
		this.metricsFolder = metricsFolder;
	}

	public String getDatetime() {
		return this.datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	/**
	 * Informs the maximum number of projects to be downloaded and processed
	 * 
	 * @return
	 */
	public String getnProjects() {
		return String.valueOf(this.nProjects);
	}

	/**
	 * Sets the maximum number of projects to be downloaded and processed
	 * 
	 * @param nProjects
	 */
	public void setnProjects(int nProjects) {
		this.nProjects = nProjects;
	}

	public List<String> getArguments() {
		return this.arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	/**
	 * Sets the forge where the search for projects will be performed Valid
	 * options are those specified in the {@link SupportedForge} enumerator
	 * 
	 * @param forge
	 */
	public void setForge(SupportedForge forge) {
		this.forge = forge;
	}

	public String getMetricsFormat() {
		return metricsFormat;
	}

	public void setMetricsFormat(String metricsFormat) {
		this.metricsFormat = metricsFormat;
	}

	public JsonInputFile getInputFile() {
		return input;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getGitHubOauthAcessToken() {
		return gitHubOauthAcessToken;
	}

	public void setGitHubOauthAcessToken(String gitHubOauthAcessToken) {
		this.gitHubOauthAcessToken = gitHubOauthAcessToken;
	}
	
	public File getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(File projectPath) {
		this.projectPath = projectPath;
	}

	public File getChild_path_src() {
		return child_path_src;
	}

	public void setChild_path_src(File child_path_src) {
		this.child_path_src = child_path_src;
	}

	public File getChild_path_srtc() {
		return child_path_srtc;
	}

	public void setChild_path_srtc(File child_path_srtc) {
		this.child_path_srtc = child_path_srtc;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public JsonInputFile getInput() {
		return input;
	}

	public void setInput(JsonInputFile input) {
		this.input = input;
	}
	
	@Option(name = "-in", usage = "all inputs together in one json file")
	public void setInputFile(File inputFile) {
		try {
			List<String> lines = Files.readLines(inputFile,
					Charset.defaultCharset());
			String json = Joiner.on(" ").join(lines);
			JsonInputFile args = new Gson().fromJson(json, JsonInputFile.class);
			this.input = args;
		} catch (JsonSyntaxException e) {
			throw new GroundhogException(
					"The format of the json file seems strange. Do you have looked at one of our examples?");
		} catch (IOException e) {
			throw new GroundhogException("I do not found this file "
					+ inputFile.getName()
					+ ". Does it actually exist?");
		}
	}
}