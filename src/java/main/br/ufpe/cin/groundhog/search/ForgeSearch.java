package br.ufpe.cin.groundhog.search;

import java.util.List;

import br.ufpe.cin.groundhog.Project;

/**
 * An interface that defines the forge search functionality.
 * A forge search must receive a search term and a page number (1-indexed) and
 * return a list of ForgeProjects objects. These projects can be used by a ForgeCrawler
 * to download source code.
 * @author fjsj
 *
 */
public interface ForgeSearch {
	
	/**
	 * Uses search functionality of the forge to get projects.
	 * 
	 * @param term term to be searched (ex: a project name, like h2database). 
	 * If the term is a null String then the method will return all the forge projects. 
	 * @param page 1-indexed page to get results (ie: starts with 1).
	 * 
	 * @return list of ForgeProject entities with projects info
	 * @throws Exception when something nasty happens
	 */
	public List<Project> getProjects(String term, int page) throws SearchException;

	/**
	 * Provides a dump of every repository in the forge.
	 * 
	 * @param start The search will start from the repository 
	 * with the this specified integer ID.
	 * @param limit Maximum number of projects to be returned. Negative values 
	 * for this parameter will result in an unbounded search.
	 * 
	 * @return list of ForgeProject entities with projects info
	 * @throws Exception when something nasty happens
	 */
	public List<Project> getAllForgeProjects(int start , int limit) throws SearchException;
	
}