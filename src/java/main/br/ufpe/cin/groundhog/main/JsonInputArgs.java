package br.ufpe.cin.groundhog.main;

import java.util.List;

public final class JsonInputArgs {
	private final String forge;
	private final String dest;
	private final String out;
	private final String datetime;
	private final String nprojects;
	private final String outputformat;
	private final Search search;

	public JsonInputArgs(String forge, String dest, String out,
			String datetime, String nprojects, String outputformat,
			Search search) {
		this.forge = forge;
		this.dest = dest;
		this.out = out;
		this.datetime = datetime;
		this.nprojects = nprojects;
		this.outputformat = outputformat;
		this.search = search;
	}

	public String getForge() {
		return forge;
	}

	public String getDest() {
		return dest;
	}

	public String getOut() {
		return out;
	}

	public String getDatetime() {
		return datetime;
	}

	public String getNprojects() {
		return nprojects;
	}

	public String getOutputformat() {
		return outputformat;
	}

	public Search getSearch() {
		return search;
	}
}

class Search {
	private final List<String> projects;
	private final String username;

	public Search(List<String> projects, String username) {
		super();
		this.projects = projects;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public List<String> getProjects() {
		return projects;
	}
}