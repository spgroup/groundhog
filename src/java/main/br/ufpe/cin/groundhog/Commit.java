package br.ufpe.cin.groundhog;

import java.util.Date;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a Commit object in Groundhog
 * @author Rodrigo Alves
 */
public class Commit implements GitHubEntity {
	@SerializedName("sha")
	private String sha;
	
	@SerializedName("commiter")
	private User commiter;
	
	@SerializedName("message")
	private String message;
	
	private Commit parent;
	
	private Project project;
	
	private Date commitDate;
	private Date pushDate;
	
	private int totalCount;
	
	private int additionsCount;
	
	private int deletionsCount;
	
	public Commit() {	
	}
	
	public Commit(String sha) {
		this.sha = sha;
	}
	
	public Commit(String sha, Project project) {
		this(sha);
		this.project = project;
	}
	
	/**
	 * Informs the SHA checksum of the commit
	 * @return
	 */
	public String getSha() {
		return this.sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	/**
	 * Informs the User who authored the commit
	 * @return
	 */
	public User getCommiter() {
		return this.commiter;
	}

	public void setCommiter(User commiter) {
		this.commiter = commiter;
	}

	/**
	 * Informs the commit message
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Informs the project to which the commit belongs
	 * @return a {@link Project} object
	 */
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Informs the date when the commit was done 
	 * @return a {@link Date} object
	 */
	public Date getCommitDate() {
		return this.commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

	/**
	 * Returns the parent {@link Commit} if it exists
	 * @return a {@link Commit} object
	 */
	public Commit getParent() {
		return this.parent;
	}

	public void setParent(Commit parent) {
		this.parent = parent;
	}

	/**
	 * Returns the date when the commit was pushed to GitHub
	 * This date should always be greater than the commit date
	 * @return
	 */
	public Date getPushDate() {
		return this.pushDate;
	}

	public void setPushDate(Date pushDate) {
		this.pushDate = pushDate;
	}

	/**
	 * Informs the sum of lines changed (added or deleted) among the files involved in the commit
	 * @return
	 */
	public int getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * Informs the sum of added lines among the files committed
	 * @param deletionsCount
	 */
	public int getAdditionsCount() {
		return this.additionsCount;
	}

	public void setAdditionsCount(int additionsCount) {
		this.additionsCount = additionsCount;
	}

	public int getDeletionsCount() {
		return this.deletionsCount;
	}

	/**
	 * Informs the sum of deleted lines among the files committed
	 * @param deletionsCount
	 */
	public void setDeletionsCount(int deletionsCount) {
		this.deletionsCount = deletionsCount;
	}

	@Override
	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s/commits",
				this.project.getUser().getLogin(), this.project.getName());
	}
}