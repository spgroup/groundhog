package br.ufpe.cin.groundhog;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.ufpe.cin.groundhog.util.Dates;

/**
 * Represents a software project in Groundhog
 * @author fjsj, gustavopinto, Rodrigo Alves
 */
public class Project implements GitHubEntity {
	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("language")
	private String language;
	private List<Language> languages;

	private User user;
	private SCM scm;

	@SerializedName("clone_url")
	private String scmURL;

	@SerializedName("html_url")
	private String sourceCodeURL;

	@SerializedName("created_at")
	private Date createdAt;

	@SerializedName("pushed_at")
	private Date lastPushedAt;

	@SerializedName("fork")
	private boolean isFork;

	@SerializedName("has_downloads")
	private boolean hasDownloads;

	@SerializedName("has_issues")
	private boolean hasIssues;

	@SerializedName("has_wiki")
	private boolean hasWiki;

	@SerializedName("watchers_count")
	private int watchersCount;

	@SerializedName("forks")
	private int forks_count;

	@SerializedName("open_issues_count")
	private int issuesCount;

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

	public Project(String name, String description, SCM scm, String scmURL) {
		this(name, description);
		this.scm = scm;
		this.scmURL = scmURL;
	}

	public Project(String name, String description, SCM scm, String scmURL,
			String sourceCodeURL) {
		this(name, description, scm, scmURL);
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
	public User getOwner() {
		return this.user;
	}

	/**
	 * Informs the project's author name
	 * @param owner a {@link String} for the name of the project's author
	 */
	public void setOwner(User owner) {
		this.user = owner;
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
		return this.hasDownloads;
	}

	/**
	 * Sets if a project allows downloads or not
	 * @param hasDownloads a {@link boolean} for setting whether the project enables downloads or not
	 */
	public void setHasDownloads(boolean hasDownloads) {
		this.hasDownloads = hasDownloads;
	}

	/**
	 * @return true if the project has issues. Returns false otherwise.
	 */
	public boolean hasIssues() {
		return this.hasIssues;
	}

	/**
	 * @param hasIssues a boolean value for setting if the project has Issues or not
	 */
	public void setHasIssues(boolean hasIssues) {
		this.hasIssues = hasIssues;
	}

	/**
	 * Informs whether the project has its own Wiki or not.
	 * @return true if the project has a Wiki. Returns false otherwise.
	 */
	public boolean hasWiki() {
		return this.hasWiki;
	}

	/**
	 * Sets whether a project has its own Wiki or not.
	 * @param hasWiki a {@link boolean} for setting whether the project has a Wiki or not.
	 */
	public void setHasWiki(boolean hasWiki) {
		this.hasWiki = hasWiki;
	}

	/**
	 * Informs how many people are watching the project or have "starred" it
	 * @return an {@link integer} informing how many people are currently watching the project on its forge
	 */
	public int getWatchersCount() {
		return this.watchersCount;
	}

	/**
	 * Sets how many people are watching the project
	 * @param watchersCount an integer for setting the number of people watching the project on its forge
	 */
	public void setWatchersCount(int watchersCount) {
		this.watchersCount = watchersCount;
	}

	/**
	 * Informs the number of forks the project has
	 * @return an integer correspondent to the number of forks
	 */
	public int getForksCount() {
		return this.forks_count;
	}

	/**
	 * An indicator of how many times the project has been forked.
	 * @param forksCount an integer for setting the number of forks the project has.
	 */
	public void setForksCount(int forksCount) {
		this.forks_count = forksCount;
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
	 * @param createdAtParam the String correspondent to the creation date of the project in question. e.g: 2012-04-28T15:40:35Z
	 */
	public void setCreatedAt(String createdAtParam) {
		Date createAtDate = new Dates("yyyy-MM-dd HH:mm:ss").format(createdAtParam);
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
	 * @param lastPushedAtParam the String correspondent to the date of the last push to the project
	 * in question. e.g: 2012-04-28T15:40:35Z
	 */
	public void setLastPushedAt(String lastPushedAtParam){
		Date lastPushDate = new Dates("yyyy-MM-dd HH:mm:ss").format(lastPushedAtParam);
		this.lastPushedAt = lastPushDate;
	}

	/**
	 * Informs the name of prevailing programming language in a project
	 * @return name of programming language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * Sets the name of prevailing programming language in a project
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Returns the list of languages that compose the Project
	 * @return a {@link List} of {@link Language} objects
	 */
	public List<Language> getLanguages() {
		return this.languages;
	}

	/**
	 * Sets the list of languages that compose the Project
	 * @param a {@link List} of {@link Language} objects
	 */
	public void setLanguages(List<Language> langs) {
		this.languages = langs;
	}

	/**
	 * Method to inform the median number of forks per project in a collection of projects
	 * @param projects
	 * @return
	 */
	public double getMedianForksRate(List<Project> projects) {
		int i = 0, listSize = projects.size(), half = listSize/2;
		double median = 0.0;
		int[] forkStore = new int[listSize];

		if (listSize == 0) {
		    throw new IllegalArgumentException("List of projects can't be empty");
		}

		for (; i < listSize; i++) {
			forkStore[i] = projects.get(i).getForksCount();
		}

		Arrays.sort(forkStore);

		if (listSize % 2 == 0) {
			median = (forkStore[half - 1] + forkStore[half])/2;
		} else {
			median = forkStore[half];
		}

		return median;
	}

	/**
	 * Method to inform the average number of forks per project in a collection of projects
	 * @param projects
	 * @return
	 */
	public static int getAverageForksRate(List<Project> projects) {
		int i = 0, j = 0, total = 0, listSize = projects.size();
		int[] forkStore = new int[listSize];

		if (listSize == 0) {
		    throw new IllegalArgumentException("List of projects can't be empty");
		}

		for (; i < listSize; i++) {
			forkStore[i] = projects.get(i).getForksCount();
		}

		for (; j < forkStore.length; j++) {
			total += forkStore[j];
		}

		return total/listSize;
	}

	/**
	 * Method to discover the percentage of projects that have forks
	 * @param The list of projects to be analyzed
	 * @return a double result - such that 0 <= result <= 1 - indicating how many of the informed projects have forks (were forked at least once)
	 */
	public static double getProjectsWithForksRate(List<Project> projects) {
		double result = 0.0;
		int projectsWithForks = 0, i = 0, listSize = projects.size();

		if (listSize == 0) {
		    throw new IllegalArgumentException("List of projects can't be empty");
		}

		for (; i < listSize; i++) {
			if (projects.get(i).getForksCount() > 0) {
				projectsWithForks++;
			}
		}

		result = (projectsWithForks / listSize);
		return result;
	}

	/**
	 * Informs what is the overall percentage of the given projects that are forks
	 * With this method we can answer the question "What is the overall percentage of Github projects that ARE forks?"
	 * @return a double result - such that 0 <= result <= 1 - indicating how many of the informed projects are forks
	 */
	public static double getProjectsThatAreForks(List<Project> projects) {
		double result = 0.0;
		int projectsAreForks = 0, i = 0, listSize = projects.size();

		if (listSize == 0) {
		    throw new IllegalArgumentException("List of projects can't be empty");
		}

		for (; i < listSize; i++) {
			if (projects.get(i).isFork()) {
				projectsAreForks++;
			}
		}

		result = (projectsAreForks / listSize);
		return result;
	}

	/**
	 * Returns the {@link User} object who is the author of the Project
	 * @return
	 */
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s", this.getUser().getLogin(), this.getName());
	}

	@Override
	public String toString() {
		return String.format("Project(%s, %s, %s)", this.name, this.description, this.sourceCodeURL);
	}
}