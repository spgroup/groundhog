package br.ufpe.cin.groundhog;

import java.util.Date;

/**
 * Represents a GitHub Organization in Groundhog
 * @author gustavopinto, rodrigoalvesvieira
 */
public class Organization {
	private String name;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}