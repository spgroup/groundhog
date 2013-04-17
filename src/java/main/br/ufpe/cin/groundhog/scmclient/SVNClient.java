package br.ufpe.cin.groundhog.scmclient;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

public class SVNClient {
	private static SVNClient instance;
	
	public static SVNClient getInstance() {
		if (instance == null) {
			instance = new SVNClient();
		}
		return instance;
	}
	
	private SVNClientManager svn;
	
	private SVNClient() {
		svn = SVNClientManager.newInstance();
	}
	
	public void checkout(String url, File destination) throws SVNException {
		SVNUpdateClient client = svn.getUpdateClient();
		client.doCheckout(SVNURL.parseURIDecoded(url),
				destination,
				SVNRevision.HEAD,
				SVNRevision.HEAD,
				SVNDepth.INFINITY,
				false);
	}
	
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
		instance = null;
	}
}