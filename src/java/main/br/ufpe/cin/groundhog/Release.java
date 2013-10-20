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
	
	public Release(Project project, int id) {
		this.project = project;
		this.id = id;
	}



	/*Get's and Set's bellow*/

	/*lack comments to Get's and Set's bellow*/

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTagName() {
		return this.tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTargetCommitish() {
		return this.targetCommitish;
	}

	public void setTargetCommitish(String targetCommitish) {
		this.targetCommitish = targetCommitish;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isDraft() {
		return this.draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public boolean isPrerelease() {
		return this.prerelease;
	}

	public void setPrerelease(boolean prerelease) {
		this.prerelease = prerelease;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getPublishedAt() {
		return this.publishedAt;
	}

	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	/*End get and set*/

	
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
