package br.cin.ufpe.epona.scm.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AllCommitFilter;
import org.gitective.core.filter.commit.CommitterDateFilter;

public class GitClient {
	
	private static GitClient instance;
	
	public static GitClient getInstance() {
		if (instance == null) {
			instance = new GitClient();
		}
		return instance;
	}
	
	private GitClient() {
		
	}
	
	public void clone(String url, File destination)
			throws InvalidRemoteException, TransportException, GitAPIException {
		Repository rep = Git.cloneRepository().
		setURI(url).
		setDirectory(destination).
		call().getRepository();
		rep.close();
	}
	
	public void checkout(File repositoryFolder, Date date)
			throws IOException,RefAlreadyExistsException, RefNotFoundException,
			InvalidRefNameException, CheckoutConflictException, GitAPIException {
		Git git = Git.open(repositoryFolder);
		Repository rep = git.getRepository();
		CommitFinder finder = new CommitFinder(rep);
		final List<RevCommit> commits = new ArrayList<RevCommit>();
		
		finder.setFilter(new CommitterDateFilter(date).negate());
		AllCommitFilter filter = new AllCommitFilter() {
			@Override
			public boolean include(RevWalk walker, RevCommit commit) throws IOException {
				boolean include = super.include(walker, commit);
				if (include) {
					commits.add(commit);
				}
				return include;
			}
		};
		finder.setMatcher(filter);
		finder.find();
		
		RevCommit closest = Collections.max(commits, new Comparator<RevCommit>() {
			public int compare(RevCommit c1, RevCommit c2) {
				return c1.getCommitterIdent().getWhen().compareTo(c2.getCommitterIdent().getWhen());
			}
		});
		git.checkout().setName("to-analyze").setStartPoint(closest).setCreateBranch(true).call();
		rep.close();
	}
	
}
