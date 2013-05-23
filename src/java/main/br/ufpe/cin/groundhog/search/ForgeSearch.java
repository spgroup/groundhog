package br.ufpe.cin.groundhog.search;

import java.util.List;

import br.ufpe.cin.groundhog.Project;

/**
 * An interface that defines the forge search functionality.
 * A forge search must receive a search term and a page number (1-indexed) and
 * return a list of ForgeProjects objects. These projects can be used by a ForgeCrawler
 * to download source code.
 * 
 * @author fjsj, gustavopinto
 */
public interface ForgeSearch {
	
	/**
	 * Uses search functionality of the forge to get projects.
	 * 
	 * @param term term to be searched (ex: a project name, like h2database)
	 * @param page 1-indexed page to get results (ie: starts with 1)
	 * 
	 * @return list of ForgeProject entities with projects info
	 * @throws SearchException when something nasty happens
	 */
	public List<Project> getProjects(String term, int page) throws SearchException;
	
	/**
	 * Uses search functionality of the forge to get projects.
	 * 
	 * @param term term to be searched (ex: a project name, like h2database)
	 * @parm username the user that should have the aforementioned project
	 * @param page 1-indexed page to get results (ie: starts with 1)
	 * 
	 * @return list of ForgeProject entities with projects info
	 * @throws SearchException when something nasty happens
	 */
	public List<Project> getProjects(String term, String username, int page) throws SearchException;

}