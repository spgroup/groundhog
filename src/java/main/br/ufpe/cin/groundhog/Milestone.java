package br.ufpe.cin.groundhog;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

/**
 * Represents a Milestone object in Groundhog
 * @author Rodrigo Alves
 */
@Entity("milestones")
public class Milestone extends GitHubEntity {
    @Indexed(unique=true, dropDups=true)
	@SerializedName("id")
	@Id private int id;
	
	@SerializedName("number")
	private int number;
	
	@Reference private Project project;

	@SerializedName("state")
	private String state;
	
	@SerializedName("title")
	private String title;
	
	@SerializedName("description")
	private String description;
	
	@SerializedName("creator")
	@Reference private User creator;
	
    private List<IssueLabel> labels;
	
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
	
	public List<IssueLabel> getLabels() {
		return this.labels;
	}

	public void setLabels(List<IssueLabel> labels) {
		this.labels = labels;
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

	/**
	 * Two {@link Milestone} objects are considered equal when they have the same GitHub API ID,
	 * the same number and belong to the same {@link Project}. 
	 * @param issue
	 * @return
	 */
	public boolean equals(Milestone ms) {
		return this.id == ms.id && this.number == ms.number && this.project.equals(ms.getProject());
	}
	
	@Override
	public String getURL() {
		String result = String.format("https://api.github.com/repos/%s/%s/issues/%d",
				this.getProject().getOwner().getLogin(), this.getProject().getName(), this.getNumber());
		
		return result;
	}

	@Override
	public String toString() {
		return "Milestone number = " + number + ", "
				+ (this.state != null ? "state = " + this.state + ", " : "")
				+ (this.title != null ? "title = " + this.title + ", " : "")
				+ (this.getURL() != null ? "url= " + this.getURL() : " ");
	}
}