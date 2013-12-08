package br.ufpe.cin.groundhog;

/**
 * Base interface for all GitHub API entities representations in Groundhog
 * @author Rodrigo Alves
 *
 */
public abstract class GitHubEntity {
	
	/**
	 * Returns the Entity's API URL
	 * @return a String 
	 */
	public abstract String getURL();
}