package br.ufpe.cin.groundhog.codehistory;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;

import br.ufpe.cin.groundhog.scmclient.SVNClient;
import br.ufpe.cin.groundhog.util.FileUtil;

public class SvnCodeHistory implements CodeHistory {
	
	@Override
	public File checkoutToDate(String project, String url, Date date) throws CheckoutException {
		try {
			File projectFolder = new File(FileUtil.getInstance().createTempDir(), project);
			SVNClient.getInstance().checkout(url, projectFolder, SVNRevision.create(date));
			return projectFolder;
		} catch (SVNException e) {
			throw new CheckoutException(e);
		}
	}

	@Override
	public File checkoutToDate(String project, File repositoryFolder, Date date) {
		throw new NoSuchMethodError("Not implemented");
	}
	
	public static void main(String[] args) throws Exception {
		Date d = new GregorianCalendar(2009, 2, 10, 18, 32, 17).getTime();
		System.out.println(d);
		new SvnCodeHistory().checkoutToDate("geom-java",
				"http://wkhtmltopdf.googlecode.com/svn/",
				d);
	}
}