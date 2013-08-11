package br.ufpe.cin.groundhog.codehistory;

import java.io.File;
import java.util.Date;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;

import br.ufpe.cin.groundhog.scmclient.SVNClient;
import br.ufpe.cin.groundhog.util.FileUtil;

import com.google.inject.Inject;

/**
 * The code history analysis implementation for the SVN SCM
 * @author fjsj
 */
public class SvnCodeHistory implements CodeHistory {
	private final SVNClient svnClient;

	@Inject
	public SvnCodeHistory(SVNClient svnClient) {
		this.svnClient = svnClient;
	}

	@Override
	public File checkoutToDate(String project, File repositoryFolder, Date date) {
		File projectFolder = new File(FileUtil.getInstance()
				.createTempDir(), project);
		try {
			this.svnClient.checkout(repositoryFolder.getName(), projectFolder, SVNRevision.create(date));
			return projectFolder;
		} catch (SVNException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException();
		}
	}
}