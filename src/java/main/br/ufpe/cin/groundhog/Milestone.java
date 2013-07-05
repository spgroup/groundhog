package br.ufpe.cin.groundhog;

import java.util.Date;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a Milestone object in Groundhog
 * @author Rodrigo Alves
 */
public class Milestone implements GitHubEntity {
	@SerializedName("id")
	private int id;
	
	@SerializedName("number")
	private int number;
	
	private Project project;

	@SerializedName("state")
	private String state;
	
	@SerializedName("title")
	private String title;
	
	@SerializedName("description")
	private String description;
	
	@SerializedName("creator")
	private User creator;
	
	@SerializedName("open_issues")
	private int openIssuesCount;
	
	@SerializedName("closed_issues")
	private int closedIssuesCount;
	
	@SerializedName("created_at")
	private Date createdAt;
	
	@SerializedName("updated_at")
	private Date updatedAt;
	
	@SerializedName("due_on")
	private Date dueOn;
	
	/**
	 * Default constructor for {@link Milestone} objects
	 * @param project the {@link Project} to which the {@link Milestone} belongs
	 * @param id the GitHub API ID of the {@link Milestone}
	 * @param number the number of the {@link Milestone}
	 * @param state the state of the {@link Milestone}
	 */
	public Milestone(Project project, int id, int number, String state) {
		this.project = project;
		this.id = id;
		this.number = number;
		this.state = state;
	}
	
	public Milestone(Project project, int id, int number, String state, String title) {
		this(project, id, number, state);
		this.title = title;
	}
	
	/**
	 * Informs the Milestone ID on GitHub. This id is unique to GitHub. No two Milestones can exist on GitHub
	 * with the same ID
	 * @return
	 */
	public int getId() {
		return this.id;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	
	/**
	 * Informs the number of the Milestone. This attribute is unique in respect to the Milestone's {@link Project}.
	 * No two Milestones can exist with the same number in the same Project
	 * @return
	 */
	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Informs the {@link Project} object to which the {@link Milestone} belongs
	 * @return a {@link Project} object
	 */
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Returns the state of the Milestone. Valid values are "open" and "closed"
	 * @return
	 */
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Informs the title of the Milestone
	 * @return a String
	 */
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Informs the description of the Milestone
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Informs the {@link User} who created the Milestone
	 * @return a {@link User} object
	 */
	public User getCreator() {
		return this.creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * Informs the number of open Issues currently in the {@link Milestone}
	 * @return an integer value
	 */
	public int getOpenIssuesCount() {
		return this.openIssuesCount;
	}

	public void setOpenIssuesCount(int openIssuesCount) {
		this.openIssuesCount = openIssuesCount;
	}

	/**
	 * Informs the number of closed Issues currently in the {@link Milestone}
	 * @return an integer value
	 */
	public int getClosedIssuesCount() {
		return this.closedIssuesCount;
	}

	public void setClosedIssuesCount(int closedIssuesCount) {
		this.closedIssuesCount = closedIssuesCount;
	}
	
	/**
	 * Informs the total number of Issues in the {@link Milestone}
	 * @return an integer value
	 */
	public int getIssuesCount() {
		return this.getOpenIssuesCount() + this.getClosedIssuesCount();
	}

	/**
	 * Informs the creation date of the Milestone
	 * @return a {@link Date} object
	 */
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	/**
	 * Informs the update date of the Milestone
	 * @return a {@link Date} object
	 */
	public Date getUpdatedAt() {
		return this.updatedAt;
	}
	
	public void setUpdateAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	/**
	 * Informs the date when the Milestone is due to
	 * @return a {@link Date} object
	 */
	public Date getDueOn() {
		return this.dueOn;
	}

	public void setDueOn(Date dueOn) {
		this.dueOn = dueOn;
	}

	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s/issues/%d",
				this.getProject().getOwner().getLogin(), this.getProject().getName(), this.getNumber());
	}

	@Override
	public String toString() {
		return "Milestone number = " + number + ", "
				+ (state != null ? "state = " + state + ", " : "")
				+ (title != null ? "title = " + title + ", " : "")
				+ (getURL() != null ? "URL = " + getURL() : "");
	}
}