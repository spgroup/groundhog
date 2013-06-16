package br.ufpe.cin.groundhog;

import java.util.Date;

/**
 * Represents a GitHub Organization in Groundhog
 * @author gustavopinto, Rodrigo Alves
 */
public class Organization implements GitHubEntity {
	private String login;
	private String name;
	private String location;
	
	private int followers;
	private int following;
	
	private Date created_at;
	
	public int public_repos;
	public int public_gists;

	public Date getCreated_at() {
		return this.created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public int getPublic_repos() {
		return this.public_repos;
	}

	public void setPublic_repos(int public_repos) {
		this.public_repos = public_repos;
	}

	public int getPublic_gists() {
		return this.public_gists;
	}

	public void setPublic_gists(int public_gists) {
		this.public_gists = public_gists;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getFollowers() {
		return this.followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFollowing() {
		return this.following;
	}

	public void setFollowing(int following) {
		this.following = following;
	}

	@Override
	public String getURL() {
		return String.format("https://api.github.com/orgs/%s", this.getLogin());
	}
}