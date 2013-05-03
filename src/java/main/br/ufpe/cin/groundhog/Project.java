package br.ufpe.cin.groundhog;

import japa.parser.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a software project in Groundhog
 * @author fjsj, gustavopinto, rodrigoalvesvieira
 *
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

	/**
	 * The 3-parameter constructor
	 * @param name the project name
	 * @param description the project description
	 * @param iconURL the project's icon URL
	 */
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

	/**
	 * 
	 * @return the name of the project
	 */
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return the String description of the project
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 
	 * @param description a String for setting the description of the project
	 */
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

	/**
	 * 
	 * @param sourceCodeURL sets the URL String of the project's source code
	 */
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

	/**
	 * 
	 * @param hasDownloads a boolean for setting whether the project enables downloads or not
	 */
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

	/**
	 * 
	 * @param hasIssues a boolean value for setting if the project has Issues or not
	 */
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

	/**
	 * 
	 * @param hasWiki a boolean for setting whether the project has a Wiki or not.
	 */
	public void setHasWiki(boolean hasWiki) {
		this.hasWiki = hasWiki;
	}

	/**
	 * 
	 * @return an integer informing how many people are currently watching the project on its forge
	 */
	public int getWatchersCount() {
		return this.watchersCount;
	}
	
	/**
	 * 
	 * @param watchersCount an integer for setting the number of people watching the project on its forge
	 */
	public void setWatchersCount(int watchersCount) {
		this.watchersCount = watchersCount;
	}

	/**
	 * 
	 * @return an integer informing the number of people following the project on its forge
	 */
	public int getFollowersCount() {
		return this.followersCount;
	}

	/**
	 * Sets how many followers the project has on its forge
	 * @param followersCount an integer, the number of people following the project
	 */
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	
	/**
	 * Informs the number of forks the project has
	 * @return an integer correspondent to the number of forks
	 */
	public int getForksCount() {
		return this.forksCount;
	}

	/**
	 * An indicator of how many times the project has been forked.
	 * @param forksCount an integer for setting the number of forks the project has.
	 */
	public void setForksCount(int forksCount) {
		this.forksCount = forksCount;
	}
	
	/**
	 * 
	 * Informs the number of open issues of the project
	 * @return an integer value correspondent to the amount of open issues
	 */
	public int getIssuesCount() {
		return this.issuesCount;
	}

	/**
	 * Sets the number of issues of a project
	 * @param issuesCount an integer for setting the number of Issues of the project
	 */
	public void setIssuesCount(int issuesCount) {
		this.issuesCount = issuesCount;
	}
	
	/**
	 * Tells whether a project is a fork of another or not
	 * @return a boolean value: true if it's a fork, false otherwise
	 */
	public boolean isFork() {
		return this.isFork;
	}
	
	/**
	 * Sets if the project is a fork of another or not
	 * @param value a boolean value for informing whether the project is a fork of another or not
	 */
	public void setIsFork(boolean value) {
		this.isFork = value;
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
		return this.createdAt;
	}

	/**
	 * Sets the creation date of the project
	 * @param createdAt a Date object for setting the creation date of the project
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	/**
	 * 
	 * @param createdAtParam the String correspondent to the creation date of the project in question. e.g: 2012-04-28T15:40:35Z
	 * @throws java.text.ParseException
	 */
	public void setCreatedAt(String createdAtParam) throws java.text.ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createAtDate = format.parse(createdAtParam.replace('T', ' ').replace("Z", ""));
		
		this.createdAt = createAtDate;
	}

	/**
	 * Returns the date of the latest push to the project
	 * @return a Date object of the latest push
	 */
	public Date getLastPushedAt() {
		return this.lastPushedAt;
	}

	/**
	 * Sets the date on which the last push has been submitted to the project's source code
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date lastPushDate = format.parse(lastPushedAtParam.replace('T', ' ').replace("Z", ""));

		this.lastPushedAt = lastPushDate;
	}

	@Override
	public String toString() {
		return String.format("Project(%s, %s, %s, %s)",
				this.name, this.description, this.sourceCodeURL, this.iconURL);
	}
}