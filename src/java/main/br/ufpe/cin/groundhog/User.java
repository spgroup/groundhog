package br.ufpe.cin.groundhog;

import java.util.Date;

/**
 * Represents a GitHub User in Groundhog
 * @author gustavopinto, Rodrigo Alves
 */
public class User implements GitHubEntity {
	private int id;
	
	private String login;
	private String email;
	private String company;
	private String location;
	private String blog;
	
	private boolean hireable;
	
	private int followers;
	private int following;
	public int public_repos;
	public int public_gists;
	
	private Date created_at;
	private Date updated_at;
	
	public User(String login) {
		this.login = login;
	}
	
	/**
	 * Informs the GitHub ID for the {@link User} object in question
	 * This ID is unique in GitHub, which means no two users can have the same ID on GitHub
	 * @return the integer ID
	 */
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
    /**
     * Informs the login (username) of the {@link User}
     * @return
     */
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Informs the {@link User}'s company name
	 * @return
	 */
	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * Informs the {@link User}'s blog URL
	 * @return
	 */
	public String getBlog() {
		return this.blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	/**
	 * Informs whether a {@link User} is available for hire (hireable) or not.
	 * @return true if the {@link User} is hireable. Returns false otherwise.
	 */
	public boolean isHireable() {
		return this.hireable;
	}

	public void setHireable(boolean hireable) {
		this.hireable = hireable;
	}

	/**
	 * Informs the sum of followers the User has
	 * @return
	 */
	public int getFollowers() {
		return this.followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	/**
	 * Informs how many other GitHub Users the User follows
	 * @return
	 */
	public int getFollowing() {
		return this.following;
	}

	public void setFollowing(int following) {
		this.following = following;
	}

	/**
	 * Informs the creation date of the {@link User}. In other words, the date
	 * when the user signed up for GitHub
	 * @return a {@link Date} object
	 */
	public Date getCreated_at() {
		return this.created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	/**
	 * Informs the last date when the {@link User} performed activity on GitHub
	 * @return a {@link Date} object
	 */
	public Date getUpdatedAt() {
		return this.updated_at;
	}

	public void setUpdatedAt(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	/**
	 * Returns the sum of public repositories that the User has
	 * @return
	 */
	public int getPublic_repos() {
		return this.public_repos;
	}

	public void setPublic_repos(int public_repos) {
		this.public_repos = public_repos;
	}
	
	/**
	 * Returns the sum of public gists that the User has
	 * @return
	 */
	public int getPublic_gists() {
		return this.public_gists;
	}

	public void setPublic_gists(int public_gists) {
		this.public_gists = public_gists;
	}

	/**
	 * Informs the email address of the {@link User}
	 * @return a String representing an email address
	 */
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * The location String of the {@link User}
	 * @return
	 */
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getURL() {
		return String.format("https://api.github.com/users/%s", this.getLogin());
	}

	@Override
	public String toString() {
		return "User id = " + id + ", " + (login != null ? "login = " + login + ", " : "")
				+ (getURL() != null ? "URL = " + getURL() : "");
	}
}