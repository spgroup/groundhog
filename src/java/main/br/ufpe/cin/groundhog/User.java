package br.ufpe.cin.groundhog;

import java.util.Date;

/**
 * Represents a GitHub User in Groundhog
 * @author gustavopinto, rodrigoalvesvieira
 */
public class User {
	private String username;
	private String company;
	private String blog;
	private boolean hirable;
	private int followers;
	private int following;
	private Date created_at;
	public int public_repos;
	public int public_gists;
	
	public String getLogin() {
		return this.username;
	}

	public void setLogin(String login) {
		this.username = login;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBlog() {
		return this.blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	public boolean isHirable() {
		return this.hirable;
	}

	public void setHirable(boolean hirable) {
		this.hirable = hirable;
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
}