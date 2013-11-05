package br.ufpe.cin.groundhog.scmclient;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

@Deprecated
public class SVNClient {
	private SVNClientManager svn;
	
	public SVNClient() {
		this.svn = SVNClientManager.newInstance();
	}
	
	/**
	 * Performs a checkout for a project with the given URL on the given destination directory
	 * @param url the project URL
	 * @param destination the destination directory
	 * @throws SVNException
	 */
	public void checkout(String url, File destination) throws SVNException {
		SVNUpdateClient client = svn.getUpdateClient();
		client.doCheckout(SVNURL.parseURIDecoded(url),
				destination,
				SVNRevision.HEAD,
				SVNRevision.HEAD,
				SVNDepth.INFINITY,
				false);
	}
	
	/**
	 * Performs a checkout for a project with the given URL on the given destination directory with a
	 * specified SVN revision
	 * @param url the project URL
	 * @param destination the destination directory
	 * @param revision the SVN revision
	 * @throws SVNException
	 */
	public void checkout(String url, File destination, SVNRevision revision) throws SVNException {
		SVNUpdateClient client = svn.getUpdateClient();
		client.doCheckout(SVNURL.parseURIDecoded(url),
				destination,
				revision,
				revision,
				SVNDepth.INFINITY,
				false);
	}
	
	public void close() {
		svn.dispose();
	}
}