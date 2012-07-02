package br.cin.ufpe.epona.codehistory;

import java.io.File;
import java.util.Date;

/**
 * An abstract class that defines the code history functionality.
 * This functionality allows temporal navigation through source code history.
 * @author fjsj
 *
 */
public abstract class CodeHistory {
	
	/**
	 * Checkouts the given project according to the given date. Returns a new temporary folder
	 * with the project source code at or before date. This variation of this method should
	 * be used when the SCM revision history is centralized within a URL (like SVN). 
	 * @param project project name
	 * @param url project SCM url, usually set by a ForgeCrawler subclass
	 * @param date date to checkout
	 * @return a new temporary folder with the project source code state at the given date.
	 * @throws Exception when something nasty happens
	 */
	public abstract File checkoutToDate(String project, String url, Date date) throws Exception;
	
	/**
	 * Checkouts the given project according to the given date. Returns a new temporary folder
	 * with the project source code at or before date. This variation of this method should
	 * be used when the SCM revision history is available locally (like Git)
	 * or when project is composed by compressed files (like SourceForge).
	 * @param project project name
	 * @param repositoryFolder project repository folder (must have all revision history locally, as in Git)
	 * @param date date to checkout
	 * @return a new temporary folder with the project source code state at the given date.
	 * @throws Exception when something nasty happens
	 */
	public abstract File checkoutToDate(String project, File repositoryFolder, Date date) throws Exception;

}
