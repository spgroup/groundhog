package br.ufpe.cin.groundhog;

import java.util.Date;

import br.ufpe.cin.groundhog.util.Dates;

/**
 * Represents a software project in Groundhog
 * @author fjsj, gustavopinto, rodrigoalvesvieira
 */
public class Project {
	private String name;
	private String description;
	private String owner;
	private SCM scm;
	private String scmURL;
	private String sourceCodeURL;
	private String language;
	
	private Date created;
	private Date pushed;

	private boolean fork;
	private boolean has_downloads;
	private boolean has_issues;
	private boolean has_wiki;

	private int watchers;
	private int followers;
	private int forks;
	private int open_issues;
	
	public Project() {
	}
	
	/**
	 * 2-parameter constructor
	 * @param name the project name
	 * @param description description the project description
	 */
	public Project(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * 3-parameter constructor
	 * @param name the project name
	 * @param description the project description
	 * @param sourceCodeURL the project's source code URL
	 */
	public Project(String name, String description, String sourceCodeURL) {
		this(name, description);
		this.sourceCodeURL = sourceCodeURL;
	}
	
	public Project(String name, String description, String sourceCodeURL, SCM scm, String scmURL) {
		this(name, description, sourceCodeURL);
		this.scm = scm;
		this.scmURL = scmURL;
	}

	/**
	 * Informs the name of the project
	 * @return the name of the project
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name of the project
	 * @param name a {@link String} for the project's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Informs the description of the project
	 * @return the String description of the project
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description of the project
	 * @param description a String for setting the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Informs the project's author name
	 * @return a String correspondent to the name of the author of the project
	 */
	public String getOwner() {
		return this.owner;
	}

	/**
	 * Informs the project's author name
	 * @param owner a {@link String} for the name of the project's author
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * Informs the project's SCM
	 * @return
	 */
	public SCM getSCM() {
		return this.scm;
	}
	
	public void setSCM(SCM scm) {
		this.scm = scm;
	}
	/**
	 * Informs the project's SCM URL
	 * @return 
	 */
	public String getScmURL() {
		return this.scmURL;
	}
	
	/**
	 * Sets the project's SCM URL
	 * @param scmURL
	 */
	public void setScmURL(String scmURL) {
		this.scmURL = scmURL;
	}
	
	/**
	 * Informs the source code URL of the project
	 * @return a String correspondent to the source code URL of the project in question
	 */
	public String getSourceCodeURL() {
		return this.sourceCodeURL;
	}

	/**
	 * Sets the source code URL for the project
	 * @param sourceCodeURL sets the URL String of the project's source code
	 */
	public void setSourceCodeURL(String sourceCodeURL) {
		this.sourceCodeURL = sourceCodeURL;
	}

	/**
	 * Informs whether a project allow downloads or not
	 * @return true if the project allows source code download. Returns false otherwise.
	 */
	public boolean hasDownloads() {
		return this.has_downloads;
	}

	/**
	 * Sets if a project allows downloads or not
	 * @param hasDownloads a {@link boolean} for setting whether the project enables downloads or not
	 */
	public void setHasDownloads(boolean hasDownloads) {
		this.has_downloads = hasDownloads;
	}
	
	/**
	 * @return true if the project has issues. Returns false otherwise.
	 */
	public boolean hasIssues() {
		return this.has_issues;
	}

	/**
	 * @param hasIssues a boolean value for setting if the project has Issues or not
	 */
	public void setHasIssues(boolean hasIssues) {
		this.has_issues = hasIssues;
	}

	/**
	 * Informs whether the project has its own Wiki or not.
	 * @return true if the project has a Wiki. Returns false otherwise. 
	 */
	public boolean hasWiki() {
		return this.has_wiki;
	}

	/**
	 * Sets whether a project has its own Wiki or not.
	 * @param hasWiki a {@link boolean} for setting whether the project has a Wiki or not.
	 */
	public void setHasWiki(boolean hasWiki) {
		this.has_wiki = hasWiki;
	}

	/**
	 * Informs how many people are watching the project or have "starred" it
	 * @return an {@link integer} informing how many people are currently watching the project on its forge
	 */
	public int getWatchersCount() {
		return this.watchers;
	}
	
	/**
	 * Sets how many people are watching the project
	 * @param watchersCount an integer for setting the number of people watching the project on its forge
	 */
	public void setWatchersCount(int watchersCount) {
		this.watchers = watchersCount;
	}

	/**
	 * 
	 * @return an integer informing the number of people following the project on its forge
	 */
	public int getFollowersCount() {
		return this.followers;
	}

	/**
	 * Sets how many followers the project has on its forge
	 * @param followersCount an integer, the number of people following the project
	 */
	public void setFollowersCount(int followersCount) {
		this.followers = followersCount;
	}
	
	/**
	 * Informs the number of forks the project has
	 * @return an integer correspondent to the number of forks
	 */
	public int getForksCount() {
		return this.forks;
	}

	/**
	 * An indicator of how many times the project has been forked.
	 * @param forksCount an integer for setting the number of forks the project has.
	 */
	public void setForksCount(int forksCount) {
		this.forks = forksCount;
	}
	
	/**
	 * 
	 * Informs the number of open issues of the project
	 * @return an integer value correspondent to the amount of open issues
	 */
	public int getIssuesCount() {
		return this.open_issues;
	}

	/**
	 * Sets the number of issues of a project
	 * @param issuesCount an integer for setting the number of Issues of the project
	 */
	public void setIssuesCount(int issuesCount) {
		this.open_issues = issuesCount;
	}
	
	/**
	 * Tells whether a project is a fork of another or not
	 * @return a boolean value: true if it's a fork, false otherwise
	 */
	public boolean isFork() {
		return this.fork;
	}
	
	/**
	 * Sets if the project is a fork of another or not
	 * @param value a boolean value for informing whether the project is a fork of another or not
	 */
	public void setIsFork(boolean value) {
		this.fork = value;
	}
	
	/**
	 * Methods that deal with dates are below
	 * Notice that each setter method is overloaded to support Date and String parameters.
	 * When the parameter is provided as a String object, the setter method will perform the
	 * conversion to a date object
	 */
	
	/**
	 * Informs the creation date of the project
	 * @return a Date object correspondent to the project's creation date
	 */
	public Date getCreatedAt() {
		return this.created;
	}

	/**
	 * Sets the creation date of the project
	 * @param createdAt a Date object for setting the creation date of the project
	 */
	public void setCreatedAt(Date createdAt) {
		this.created = createdAt;
	}
	
	/**
	 * 
	 * @param createdAtParam the String correspondent to the creation date of the project in question. e.g: 2012-04-28T15:40:35Z
	 */
	public void setCreatedAt(String createdAtParam) {
		this.created = Dates.format("yyyy-MM-dd HH:mm:ss", createdAtParam);
	}

	/**
	 * Returns the date of the latest push to the project
	 * @return a Date object of the latest push
	 */
	public Date getLastPushedAt() {
		return this.pushed;
	}

	/**
	 * Sets the date on which the last push has been submitted to the project's source code
	 * @param lastPushedAtParam the Date object correspondent to the date of the last push to the project
	 * in question
	 */
	public void setLastPushedAt(Date lastPushedAtParam) {
		this.pushed = lastPushedAtParam;
	}
	
	/**
	 * 
	 * @param lastPushedAtParam the String correspondent to the date of the last push to the project
	 * in question. e.g: 2012-04-28T15:40:35Z
	 */
	public void setLastPushedAt(String lastPushedAtParam) {
		this.pushed = Dates.format("yyyy-MM-dd HH:mm:ss", lastPushedAtParam);
	}
	

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return String.format("Project(%s, %s, %s, %s)",
				this.name, this.description, this.sourceCodeURL);
	}
}