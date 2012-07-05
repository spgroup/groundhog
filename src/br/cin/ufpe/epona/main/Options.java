package br.cin.ufpe.epona.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

enum SupportedForge {
	GitHub, SourceForge, GoogleCode
}

public class Options {
	
	@Option(name="-forge", usage="forge to be used in search and crawling process")
	private SupportedForge forge = SupportedForge.GitHub;
	
	@Option(name="-dest", usage="destination folder into which projects will be downloaded")
	private File destinationFolder = null;
	
	@Option(name="-out", usage="output folder to metrics files")
	private File metricsFolder = null;
	
	@Option(name="-datetime", usage="datetime of projects source code to be processed")
	private String datetime = null;
	
	@Option(name="-nprojects", usage="maximum number of projects to be downloaded and processed")
	private int nProjects = 4;
	
	@Option(name="-nthreads", usage="maximum number of concurrent threads")
	private int nThreads = 4;
	
	@Argument
    private List<String> arguments = new ArrayList<String>();
	
	public SupportedForge getForge() {
		return forge;
	}

	public File getDestinationFolder() {
		return destinationFolder;
	}

	public void setDestinationFolder(File destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

	public File getMetricsFolder() {
		return metricsFolder;
	}

	public void setMetricsFolder(File metricsFolder) {
		this.metricsFolder = metricsFolder;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public int getnProjects() {
		return nProjects;
	}

	public void setnProjects(int nProjects) {
		this.nProjects = nProjects;
	}

	public int getnThreads() {
		return nThreads;
	}

	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public void setForge(SupportedForge forge) {
		this.forge = forge;
	}

}
