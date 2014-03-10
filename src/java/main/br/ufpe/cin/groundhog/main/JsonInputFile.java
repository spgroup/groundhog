package br.ufpe.cin.groundhog.main;

import java.io.File;
import java.util.Date;
import java.util.List;

import br.ufpe.cin.groundhog.codehistory.UnsupportedForgeException;
import br.ufpe.cin.groundhog.parser.java.formater.CSVFormater;
import br.ufpe.cin.groundhog.parser.java.formater.Formater;
import br.ufpe.cin.groundhog.parser.java.formater.FormaterFactory;
import br.ufpe.cin.groundhog.parser.java.formater.JSONFormater;
import br.ufpe.cin.groundhog.util.Dates;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * This class represents the parameters passed via command line. 
 * @author gustavopinto
 */
public final class JsonInputFile {
	private String forge;
	private String dest;
	private String out;
	private String datetime;
	private String nprojects;
	private String outputformat;
	private Search search;
	private String gitHubOauthAcessToken;
	private File javaprojectpath;
	private File child_path_src;
	private File child_path_srtc;
	private String dbname;

	public JsonInputFile(CmdOptions opt) {
		super();
		this.forge = opt.getForge().name();
		this.dest = opt.getDestinationFolder() != null ? opt.getDestinationFolder().getAbsolutePath() : null;
		this.out = opt.getMetricsFolder().getAbsolutePath();
		this.datetime = opt.getDatetime();
		this.nprojects = opt.getnProjects();
		this.outputformat = opt.getMetricsFormat();
		this.search = new Search(opt.getArguments(), opt.getUsername());
		this.gitHubOauthAcessToken = opt.getGitHubOauthAcessToken();
		this.javaprojectpath = opt.getProjectPath();
		this.child_path_src = opt.getChild_path_src();
		this.child_path_srtc = opt.getChild_path_srtc();
		this.dbname = opt.getDbName();
	}

	//TODO: this should be discovered dynamically
	public SupportedForge getForge() {
		if (forge.toLowerCase().equals("github")) {
			return SupportedForge.GITHUB;
		} else if (forge.equals("googlecode")) {
			return SupportedForge.GOOGLECODE;
		} else if (forge.equals("googlecode")) {
			return SupportedForge.SOURCEFORGE;			
		}
		throw new UnsupportedForgeException("Sorry, currently Groundhog only supports Github, Sourceforge and GoogleCode. We do not support: " + forge);
	}

	public File getDest() {
		return new File(this.dest);
	}

	public File getOut() { 
		return new File(this.out);
	}

	public Date getDatetime() {
		return new Dates("yyyy-MM-dd HH:mm").format(this.datetime);
	}

	public int getNprojects() {
		return Integer.parseInt(this.nprojects);
	}

	public Formater getOutputformat() {
		if(this.outputformat.equals("json")) {
			return FormaterFactory.get(JSONFormater.class);
		} 
		return FormaterFactory.get(CSVFormater.class);
	}

	public static int getMaxThreads() {
		return 4;
	}
	
	public String getGitHubOauthAcessToken() {
		return gitHubOauthAcessToken;
	}

	public Search getSearch() {
		return this.search;
	}

	public File getJavaProjectPath(){
		return this.javaprojectpath;
	}
	
	public File getJavaProjectSourceRootPath(){
		if(this.child_path_src == null){
			return null;
		}else if(this.child_path_src.isDirectory()){
			return this.child_path_src;
		}else{
			File to_return = new File(javaprojectpath, this.child_path_src.toString());
			return to_return;
		}
	}
	
	public File getJavaProjectSourceRootTestPath(){
		if(this.child_path_srtc == null){
			return null;
		}else if(this.child_path_srtc.isDirectory()){
			return this.child_path_srtc;
		}else{
			File to_return = new File(javaprojectpath, this.child_path_srtc.toString());
			return to_return;
		}
	}
	
	public String getDBName(){
		return this.dbname;
	}
	
	public String toString() {
		return Objects.toStringHelper("")
				.add("forge", forge)
				.add("dest", dest)
				.add("out", out)
				.add("datetime", datetime)
				.add("nproject", nprojects)
				.add("outputformat", outputformat)
				.add("search", search)
				.add("javaprojectpath", javaprojectpath)
				.add("javaprojectsourcerootpath", child_path_src)
				.add("javaprojectsourcetestrootpath", child_path_srtc)
				.add("dbname", dbname)
				.toString();
	}
}

final class Search {
	private List<String> projects;
	private String username;

	public Search(List<String> projects, String username) {
		super();
		this.projects = projects.size() > 0 ? projects : Lists.newArrayList("eclipse");
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public List<String> getProjects() {
		return this.projects;
	}

	public String toString() {
		return Objects.toStringHelper("")
				.add("projects", this.projects)
				.add("username", this.username)
				.toString();
	}
}