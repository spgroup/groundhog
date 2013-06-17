package br.ufpe.cin.groundhog;

import java.util.Date;

/**
 * Represents an Issue object in Groundhog
 * @author Rodrigo Alves
 */
public class Issue implements GitHubEntity {
	private int id;
	private int number;
	private int commentsCount;
	
	private Project project;
	private PullRequest pullRequest;
	private Milestone milestone;
		
	private String title;
	private String body;
	private String state;
	
	private User assignee;
	private User closedBy;
	
	private Date createdAt;
	private Date updatedAt;
	private Date closedAt;
	
	public Issue(Project project, int number, String state) {
		this.number = number;
		this.project = project;
		this.state = state;
	}
	
	public Issue(Project project, int number, String state, String title) {
		this(project, number, state);
		this.title = title;
	}

	/**
	 * Returns the ID of the Issue on GitHub. This ID is unique for every Issue on GitHub, which means that
	 * no two (or more) Issues on GitHub may have the same ID
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the number of the Issue on its Project. This number is unique within the project to which
	 * the Issues belong. Thus, the same project may not have two Issues with the same number but two (or more)
	 * different projects on GitHub may have Issues with the same number. 
	 * @return
	 */
	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Informs the number of comments on the Issue
	 * @return
	 */
	public int getCommentsCount() {
		return this.commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}
	
	/**
	 * Informs the Project object to which the Issue belongs
	 * No Issue exists without a project
	 * @return
	 */
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	/**
	 * Informs the PullRequest related to the Issue. Not all Issues are Pull Request Issues.
	 * If this method return nulls then it means the Issue is not a PullRequest Issue
	 * @return
	 */
	public PullRequest getPullRequest() {
		return this.pullRequest;
	}

	public void setPullRequest(PullRequest pullRequest) {
		this.pullRequest = pullRequest;
	}

	public Milestone getMilestone() {
		return this.milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	/**
	 * Returns the title of the Issue
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the Markdown-syntax-based description of the Issue
	 * @return
	 */
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Returns the current state of the Issue. Possible values are "open" and "closed"
	 * @return
	 */
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Returns the User assigned to that Issue.
	 * An Issue on GitHub may or may not have an assignee
	 * @return
	 */
	public User getAssignee() {
		return this.assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	/**
	 * Informs the User who closed the Issue
	 * Every Issue gets closed by someone, so if this value is null
	 * then the Issue is currently open
	 * @return
	 */
	public User getClosedBy() {
		return this.closedBy;
	}

	public void setClosedBy(User closedBy) {
		this.closedBy = closedBy;
	}

	/**
	 * Informs the creation date of the Issue on GitHub
	 * @return
	 */
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Informs the date when the last modification was made upon the issue
	 * @return
	 */
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * Informs the date when the Issue was closed
	 * If the value is null it means the issue is open
	 * @return
	 */
	public Date getClosedAt() {
		return this.closedAt;
	}

	public void setClosedAt(Date closedAt) {
		this.closedAt = closedAt;
	}
	
	/**
	 * Returns true if the Issue is open. Returns false otherwise
	 * @return
	 */
	public boolean isOpen() {
		return this.getState() == "closed" ? true : false;
	}
	
	/**
	 * Returns true if the Issue is a Pull Request Issue. Returns false otherwise
	 * @return
	 */
	public boolean isPullRequest() {
		return this.getPullRequest() != null ? true : false;
	}

	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s/issues/%d",
				this.getProject().getOwner().getLogin(), this.getProject().getName(), this.getNumber());
	}

	@Override
	public String toString() {
		return "Issue number = " + number + ", "
				+ (title != null ? "title = " + title + ", " : "")
				+ (getURL() != null ? "URL = " + getURL() : "");
	}
}