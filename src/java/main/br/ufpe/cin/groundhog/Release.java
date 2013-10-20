package br.ufpe.cin.groundhog;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**Represents a Release object in Groundhog
 **/
public class Release implements GitHubEntity {

	@SerializedName("id")
	private int id;

	@SerializedName("tag_name")
	private String tagName;
	
	@SerializedName("target_commitish")
	private String targetCommitish;

	@SerializedName("name")
	private String name;

	@SerializedName("body")
	private String body;

	@SerializedName("draft")
	private boolean draft;

	@SerializedName("prerelease")
	private boolean prerelease;

	@SerializedName("created_at")
	private Date createdAt;

	@SerializedName("published_at")
	private Date publishedAt;

	private Project project; 
	
	
	public Release(Project project, String tagName) {
		this.project = project;
		this.tagName = tagName;
	}


	/**
	 * Returns the ID of the Release on GitHub. This ID is unique for every Release on GitHub, which means that
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
	 * Returns the Tag Name of the Release on GitHub.
	 * @return the tag_name
	 */
	public String getTagName() {
		return tagName;
	}


	public void setTag_name(String tagName) {
		this.tagName = tagName;
	}


	/**
	 * Returns the commitish value that determines where the Git tag is created from. Can be any branch or commit SHA. Defaults to the repository’s default branch (usually “master”). Unused if the Git tag already exists.
	 * @return the target_commitish
	 */
	public String getTargetCommitish() {
		return targetCommitish;
	}

	public void setTarget_commitish(String targetCommitish) {
		this.targetCommitish = targetCommitish;
	}


	/**
	 * Informs the Project object to which the Release belongs
	 * No Release exists without a project
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}


	/**
	 * Returns the name of the Release on GitHub.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Returns the Markdown-syntax-based description of the Release
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}


	/**
	 * Returns true if the Release is a draft (unpublished) or false if it's published
	 * @return the draft
	 */
	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}


	/**
	 * Returns true if the Release is a prerelease or false if the Release is a full release
	 * @return the prerelease
	 */
	public boolean isPrerelease() {
		return prerelease;
	}

	public void setPrerelease(boolean prerelease) {
		this.prerelease = prerelease;
	}


	/**
	 * Returns the date when the Release was created
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	/**
	 * Returns the date when the Release was published
	 * @return the published_at
	 */
	public Date getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}

	
	@Override
	public String toString() {
		String stringReturn = "Release id = " + this.id;// we always have a Issue number, the programmer must read the doc to contribute
		if ( this.name != null ) {
			stringReturn += ", Name = " + this.name;
		}
		if ( this.targetCommitish != null ) {
			stringReturn += ", Target Commitish = " + this.targetCommitish;
		}
		String url = this.getURL(); // This class doesn't contains a variable referring a URL, so we create one local
		if ( url != null ) {
			stringReturn += ", URL = " + url;   
		}
		return stringReturn;
	}
	
	/*get URL bellow*/
	@Override
	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s/releases/%d", this.getProject().getOwner().getLogin(), this.getProject().getName(), this.id);
	}
	


}
