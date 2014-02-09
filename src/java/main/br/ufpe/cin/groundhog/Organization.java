package br.ufpe.cin.groundhog;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

/**
 * Represents a GitHub Organization in Groundhog
 * @author gustavopinto, Rodrigo Alves
 */

@Entity("organizations")
public class Organization extends GitHubEntity {
    @Indexed(unique=true, dropDups=true)
	@SerializedName("id")
	@Id private int id;

	@SerializedName("login")
	private String login;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("location")
	private String location;
	
	@Reference private List<User> members;
	
	@SerializedName("created_at")
	private Date created_at;
	
	@SerializedName("updated_at")
	private Date updatedAt;
	
	@SerializedName("public_repos")
	public int public_repos;
	
	@SerializedName("public_gists")
	public int public_gists;

	/**
	 * Informs the GitHub ID for the {@link Organization} object in question
	 * This ID is unique in GitHub, which means no two organizations can have the same ID on GitHub
	 * @return the integer ID
	 */
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Informs the creation date of the Organization
	 * @return a {@link Date} object correspondent to the Organization's creation date
	 */
	public Date getCreated_at() {
		return this.created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	/**
	 * Informs the updated at of the Organization.
	 * That means the date when the last activity occurred in one of the projects of the Organization
	 * @return a {@link Date} object correspondent to the Organization's update (last activity) date
	 */
	public Date getUpdatedAt() {
		return this.updatedAt;
	}
	
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * Informs how many public projects the Organization has
	 * @return an integer value
	 */
	public int getPublic_repos() {
		return this.public_repos;
	}

	public void setPublic_repos(int public_repos) {
		this.public_repos = public_repos;
	}

	/**
	 * Informs how many public gists the Organization has
	 * @return an integer value
	 */
	public int getPublic_gists() {
		return this.public_gists;
	}

	public void setPublic_gists(int public_gists) {
		this.public_gists = public_gists;
	}

	/**
	 * Informs the login (username) of the Organization
	 * @return a {@link String} object
	 */
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * Informs the login (username) of the Organization
	 * @return a {@link String} object
	 */
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Informs the location of the Organization
	 * @return a {@link String} object
	 */
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getURL() {
		return String.format("https://api.github.com/orgs/%s", this.getLogin());
	}
	
	/**
	 * For two {@link Organization} objects to be considered equal they only need
	 * to refer to the same login on GitHub
	 * @param org
	 * @return
	 */
	public boolean equals(Organization org) {
		return this.login == org.login;
	}

	@Override
	public String toString() {
		return "Organization" + (login != null ? "login = " + login + ", " : "")
				+ "public repos = " + public_repos + ", public gists = "
				+ public_gists + ", "
				+ (getURL() != null ? "URL = " + getURL() : "");
	}
}