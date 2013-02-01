package br.cin.ufpe.epona.search;

import java.util.List;

import br.cin.ufpe.epona.Project;

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
	 * @param term term to be searched (ex: a project name, like h2database)
	 * @param page 1-indexed page to get results (ie: starts with 1)
	 * 
	 * @return list of ForgeProject entities with projects info
	 * @throws Exception when something nasty happens
	 */
	public List<Project> getProjects(String term, int page) throws SearchException;

}
