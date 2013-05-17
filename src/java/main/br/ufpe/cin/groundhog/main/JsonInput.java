package br.ufpe.cin.groundhog.main;

import java.util.List;

import com.google.common.base.Objects;

public final class JsonInput {
	private String forge;
	private String dest;
	private String out;
	private String datetime;
	private String nprojects;
	private String outputformat;
	private Search search;

	public JsonInput(String forge, String dest, String out, String datetime,
			String nprojects, String outputformat, Search search) {
		super();
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

	public String toString() {
		return Objects.toStringHelper(this)
					.add("forge", forge)
					.add("dest", dest)
					.add("out", out)
					.add("datetime", datetime)
					.add("nproject", nprojects)
					.add("outputformat", outputformat)
					.add("search", search)
					.toString();
	}
}

final class Search {
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
	
	public String toString() {
		return Objects.toStringHelper(this)
				.add("projects", projects)
				.add("username", username)
				.toString();
	}
}