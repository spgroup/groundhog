package br.ufpe.cin.groundhog;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

/** <p>This class represents a Release in Groundhog 
 * 
 * <p> <i>"Releases are first-class objects with changelogs and binary 
 * assets that present a full project history beyond Git artifacts.
 *  They're accessible from a repository's homepage" </i> 
 *  - Release description at GitHub oficial website, for more informations you can check at this <a href= https://github.com/blog/1547-release-your-software> link </a>.
 *	 
 *@see
 * <p>This class have a list of attributes whom <b> should be set </b> after the instantiation. These attributes represent the entities of a Release on the GitHub, see they below.   
 * <p>The attributes are at form bellow:    
 * <p>Class/type attributeName: What he represent  
 * <ul>
 * 		 <li> String tagName: Tag Name of the Release on GitHub. 
 *		 <li> String name: The name of the Release on GitHub.
 *		 <li> String targetCommitish: The Commitish value that determines where the Git tag is created from. Can be any branch or commit SHA. Defaults to the repository’s default branch (usually “master”). Unused if the Git tag already exists.
 *		 <li> String body: The Markdown-syntax-based description of the Release.
 * 		 <li> String assetsUrl: That present a full project history beyond Git artifacts
 * 		 <li> boolean draft: If the Release is a draft (unpublished) or not.
 *		 <li> booelan preRelease: If the Release is or not a full release.
 *		 <li> Date createdAt: When the Release was created.
 *		 <li> Date publishedAt: When the Release was published.
 *		
 * </ul> 	
 * <p> These descriptions can also be seen in the Get methods.
 * @author Marlon Reghert (mras) & Tomer Simis (tls)
 * 

 *
 **/

@Entity("releases")
public class Release extends GitHubEntity {
    @Indexed(unique=true, dropDups=true)
	@SerializedName("id")
	@Id private int id;

	@SerializedName("tag_name")
	private String tagName;

	@SerializedName("target_commitish")
	private String targetCommitish;

	@SerializedName("name")
	private String name;

	@SerializedName("assets_url")
	private String assetsUrl;

	@SerializedName("body")
	private String body;

	@SerializedName("draft")
	private boolean draft;

	@SerializedName("preRelease")
	private boolean preRelease;

	@SerializedName("created_at")
	private Date createdAt;

	@SerializedName("published_at")
	private Date publishedAt;

	@Reference private Project project; 

	public Release(Project project, int id) {
		this.project = project;
		this.id = id;
	}

	public Release(Project project, int id, String tagName) {
		this.project = project;
		this.id = id;
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
		return this.tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Returns the commitish value that determines where the Git tag is created from. Can be any branch or commit SHA. Defaults to the repository’s default branch (usually “master”). Unused if the Git tag already exists.
	 * @return the target_commitish
	 */
	public String getTargetCommitish() {
		return targetCommitish;
	}

	public void setTargetCommitish(String targetCommitish) {
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

	/**Return the String to Assets url, that present a full project history beyond Git artifacts*/
	public String getAssetsUrl() {
		return this.assetsUrl;
	}

	public void setAssetsUrl(String assetsUrl) {
		this.assetsUrl = assetsUrl;
	}

	/**
	 * Returns the Markdown-syntax-based description of the Release
	 * @return the body
	 */
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Returns true if the Release is a draft (unpublished) or false if it's published
	 * @return the draft
	 */
	public boolean isDraft() {
		return this.draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	/**
	 * Returns true if the Release is a preRelease or false if the Release is a full release
	 * @return the preRelease
	 */
	public boolean isPreRelease() {
		return this.preRelease;
	}

	public void setPreRelease(boolean preRelease) {
		this.preRelease = preRelease;
	}

	/**
	 * Returns the date when the Release was created
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Returns the date when the Release was published
	 * @return the published_at
	 */
	public Date getPublishedAt() {
		return this.publishedAt;
	}

	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}
	
	/**
	 * Two {@link Release} objects are considered equal if and only if they share the same
	 * GitHub API ID and belong to the same {@link Project}
	 * @param rel
	 * @return
	 */
	public boolean equals(Release rel) {
		return this.id == rel.id && this.project.equals(rel.getProject());
	}

	/**Return a String representation of the Release, this String contains (if they are not null):
	 * <ul>
	 * 		<li> The Release id
	 * 		<li> The Target Commitish
	 * 	    <li> A URL representation of the Release	
	 * </ul>
	 * 
	 * */
	@Override
	public String toString() {
		String stringReturn = "Release id = " + this.id;// we always have a Issue number, the programmer must read the doc to contribute
		
		if (this.name != null) stringReturn += ", Name = " + this.name;
		if (this.targetCommitish != null) stringReturn += ", Target Commitish = " + this.targetCommitish;
		
		String url = this.getURL(); // This class doesn't contain a variable referring to a URL, so we create one locally
		if (url != null) stringReturn += ", URL = " + url;
		
		return stringReturn;
	}

	/**This method return the URL of the release*/
	@Override
	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s/releases/%d",
				this.getProject().getOwner().getLogin(),
				this.getProject().getName(), this.id
			  );
	}
}