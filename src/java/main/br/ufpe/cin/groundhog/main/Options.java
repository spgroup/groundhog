package br.ufpe.cin.groundhog.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

enum SupportedForge {
	GITHUB, SOURCEFORGE, GOOGLECODE
}

enum MetricsOutputFormat {
	JSON, CSV
}

/**
 * The command-line options parsing class
 * 
 * @author fjsj, gustavopinto, rodrigoalvesvieira
 */
public class Options {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd_HH_mm");

	@Option(name = "-forge", usage = "forge to be used in search and crawling process")
	private SupportedForge forge = SupportedForge.GITHUB;

	@Option(name = "-dest", usage = "destination folder into which projects will be downloaded")
	private File destinationFolder = null;

	@Option(name = "-out", usage = "output folder to metrics files")
	private File metricsFolder = null;

	@Option(name = "-datetime", usage = "datetime of projects source code to be processed")
	private String datetime = dateFormat.format(new Date());

	@Option(name = "-nprojects", usage = "maximum number of projects to be downloaded and processed")
	private int nProjects = 4;

	@Option(name = "-o", usage = "determine the output format of the metrics")
	private MetricsOutputFormat metricsFormat = MetricsOutputFormat.JSON;

	private JsonInput input = null;

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

	public Date getDatetime() throws ParseException {
		return dateFormat.parse(this.datetime);
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	/**
	 * Informs the maximum number of projects to be downloaded and processed
	 * 
	 * @return
	 */
	public int getnProjects() {
		return this.nProjects;
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

	public static SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public MetricsOutputFormat getMetricsFormat() {
		return metricsFormat;
	}

	public void setMetricsFormat(MetricsOutputFormat metricsFormat) {
		this.metricsFormat = metricsFormat;
	}

	public JsonInput getInputFile() {
		return input;
	}

	@Option(name = "-in", usage = "all above inputs in one json file")
	public void setInputFile(File inputFile) throws FileNotFoundException {
		try {
			List<String> lines = Files.readLines(inputFile,
					Charset.defaultCharset());
			String json = Joiner.on(" ").join(lines);
			JsonInput args = new Gson().fromJson(json, JsonInput.class);
			this.input = args;
		} catch (JsonSyntaxException e) {
			throw new RuntimeException(
					"O formato do arquivo json parece estranho. De uma olhada nos nossos exemplos!");
		} catch (IOException e) {
			throw new RuntimeException("Não consegui achar o arquivo "
					+ inputFile.getName()
					+ ". Ele não está em outro diretório?");
		}
	}
}