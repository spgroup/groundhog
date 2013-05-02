package br.ufpe.cin.groundhog;

import japa.parser.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Represents a software project in Groundhog
 */
public class Project {
	private String name;
	private String description;
	private String creator;
	private String iconURL;
	private SCM scm;
	private String scmURL;
	private String sourceCodeURL;
	
	private Date createdAt; // Stores when was the project created
	private Date lastPushedAt; // Stores the when the last push occurred

	private boolean isFork; // Stores a boolean value for whether the project is a fork or not
	private boolean hasDownloads;
	private boolean hasIssues;
	private boolean hasWiki;

	private int watchersCount;
	private int followersCount;
	private int forksCount;
	private int issuesCount; // Stores the number of open issues
	
	public Project() {
	}
	
	public Project(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Project(String name, String description, String iconURL) {
		this(name, description);
		this.iconURL = iconURL;
	}
	
	public Project(String name, String description, String iconURL, String sourceCodeURL) {
		this(name, description);
		this.iconURL = iconURL;
		this.sourceCodeURL = sourceCodeURL;
	}
	
	public Project(String name, String description, String iconURL, SCM scm, String scmURL) {
		this(name, description, iconURL);
		this.scm = scm;
		this.scmURL = scmURL;
	}
	
	public Project(String name, String description, String iconURL, SCM scm, String scmURL,
			String sourceCodeURL) {
		this(name, description, iconURL, scm, scmURL);
		this.sourceCodeURL = sourceCodeURL;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return a String correspondent to the name of the author of the project
	 */
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getIconURL() {
		return this.iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	public SCM getSCM() {
		return this.scm;
	}
	
	public void setSCM(SCM scm) {
		this.scm = scm;
	}
	
	public String getScmURL() {
		return this.scmURL;
	}
	
	public void setScmURL(String scmURL) {
		this.scmURL = scmURL;
	}
	
	/**
	 * 
	 * @return a String correspondent to the source code URL of the project in question
	 */
	public String getSourceCodeURL() {
		return this.sourceCodeURL;
	}

	public void setSourceCodeURL(String sourceCodeURL) {
		this.sourceCodeURL = sourceCodeURL;
	}

	/**
	 * 
	 * @return true if the project allows source code download. Returns false otherwise.
	 */
	public boolean hasDownloads() {
		return this.hasDownloads;
	}

	public void setHasDownloads(boolean hasDownloads) {
		this.hasDownloads = hasDownloads;
	}
	
	/**
	 * 
	 * @return true if the project has issues. Returns false otherwise.
	 */
	public boolean hasIssues() {
		return this.hasIssues;
	}

	public void setHasIssues(boolean hasIssues) {
		this.hasIssues = hasIssues;
	}

	/**
	 * 
	 * @return true if the project has a Wiki. Returns false otherwise. 
	 */
	public boolean hasWiki() {
		return this.hasWiki;
	}

	public void setHasWiki(boolean hasWiki) {
		this.hasWiki = hasWiki;
	}

	public int getWatchersCount() {
		return this.watchersCount;
	}

	public void setWatchersCount(int watchersCount) {
		this.watchersCount = watchersCount;
	}

	public int getFollowersCount() {
		return this.followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	
	/**
	 * 
	 * @return an integer correspondent to the number of forks the project in question has
	 */
	public int getForksCount() {
		return this.forksCount;
	}

	public void setForksCount(int forksCount) {
		this.forksCount = forksCount;
	}
	
	/**
	 * 
	 * @return an integer correspondent to the number of open issues of the project in question
	 */
	public int getIssuesCount() {
		return this.issuesCount;
	}

	public void setIssuesCount(int issuesCount) {
		this.issuesCount = issuesCount;
	}
	
	/**
	 * 
	 * @return a boolean value informing whether the project is a fork of another or not
	 */
	public boolean isFork() {
		return this.isFork;
	}
	
	/**
	 * 
	 * @param value a boolean value for informing whether the project is a fork of another or not
	 */
	public void isFork(boolean value) {
		this.isFork = value;
	}
	
	/**
	 * Methods that deal with dates below.
	 * Notice that each setter method is overloaded to support Date and String parameters.
	 * When the parameter is provided as a String object, the setter method will perform the
	 * conversion to a date object.
	 */
	
	/**
	 * 
	 * @return the creation date of the project in question
	 */
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	/**
	 * 
	 * @param createdAtParam the String correspondent to the creation date of the project in question. e.g: 2012-04-28T15:40:35Z
	 * @throws java.text.ParseException
	 */
	public void setCreatedAt(String createdAtParam) throws java.text.ParseException {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss"); // first example
		Date createAtDate = format.parse(createdAtParam);
		
		this.createdAt = createAtDate;
	}

	/**
	 * 
	 * @return the date of the latest push to the project in question
	 */
	public Date getLastPushedAt() {
		return this.lastPushedAt;
	}

	/**
	 * 
	 * @param lastPushedAtParam the Date object correspondent to the date of the last push to the project
	 * in question
	 */
	public void setLastPushedAt(Date lastPushedAtParam) {
		this.lastPushedAt = lastPushedAtParam;
	}
	
	/**
	 * 
	 * @param lastPushedAtParam the String correspondent to the date of the last push to the project
	 * in question. e.g: 2012-04-28T15:40:35Z
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	public void setLastPushedAt(String lastPushedAtParam) throws ParseException, java.text.ParseException {		
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
		Date lastPushDate = format.parse(lastPushedAtParam);
		
		this.lastPushedAt = lastPushDate;
	}

	@Override
	public String toString() {
		return String.format("Project(%s, %s, %s, %s)",
				this.name, this.description, this.sourceCodeURL, this.iconURL);
	}
}