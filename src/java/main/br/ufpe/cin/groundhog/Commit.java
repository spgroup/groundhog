package br.ufpe.cin.groundhog;

import java.util.Date;

import br.ufpe.cin.groundhog.util.Dates;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Commit object in Groundhog
 * @author Rodrigo Alves, gustavopinto
 * @since 0.0.1
 */
public class Commit implements GitHubEntity {
	@SerializedName("sha")
	private String sha;
	
	@SerializedName("commiter")
	private User commiter;
	
	@SerializedName("message")
	private String message;
	
	private Project project;
	
	private Date commitDate;
	
	private int additionsCount;
	
	private int deletionsCount;
	
	public Commit(String sha, Project project) {
		this.sha = sha;
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

	public void setCommitDate(String date) {
		Date createAtDate = new Dates("yyyy-MM-dd HH:mm:ss").format(date);
		this.commitDate = createAtDate;
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

	/**
	 * Informs the sum of deleted lines among the files committed
	 * @param deletionsCount
	 */
	public int getDeletionsCount() {
		return this.deletionsCount;
	}

	public void setDeletionsCount(int deletionsCount) {
		this.deletionsCount = deletionsCount;
	}
	
	/**
	 * Gives the abbreviated SHA of the {@link Commit} object
	 * @return a {@link String} object
	 */
	public String getabbrevSHA() {
		return this.sha.substring(0, 7);
	}

	@Override
	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s/commits/%s",
				this.project.getUser().getLogin(), this.project.getName(), this.sha);
	}
	
	@Override
	public String toString() {
		return String.format("Commit(%s, %s)", this.commitDate, this.message);
	}
}