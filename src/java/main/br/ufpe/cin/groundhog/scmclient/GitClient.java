package br.ufpe.cin.groundhog.scmclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AllCommitFilter;
import org.gitective.core.filter.commit.CommitterDateFilter;

import br.ufpe.cin.groundhog.util.Dates;

public class GitClient {

	/**
	 * Performs a clone operation for the given project URL and places the fetched code
	 * into the destination directory.
	 * @param url the project's URL
	 * @param destination 
	 */
	public void clone(String url, File destination)
			throws InvalidRemoteException, TransportException, GitAPIException {
		Git git = Git.cloneRepository().setURI(url).setDirectory(destination).call();
		git.getRepository().close();
	}

	/**
	 * Performs a checkout to the given Git repository
	 * @param repositoryFolder the repository where the checkout will be performed
	 * @param date
	 * @throws IOException
	 * @throws RefAlreadyExistsException
	 * @throws InvalidRefNameException
	 * @throws CheckoutConflictException
	 * @throws GitAPIException
	 * @throws EmptyProjectAtDateException
	 */
	public void checkout(File repositoryFolder, Date date)
			throws IOException, RefAlreadyExistsException,
			InvalidRefNameException, CheckoutConflictException, GitAPIException,
			EmptyProjectAtDateException {
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

		if (commits.size() == 0) {
			rep.close();
			throw new EmptyProjectAtDateException(new Dates("yyyy-MM-dd").format(date));
		}
		RevCommit closest = Collections.max(commits, new Comparator<RevCommit>() {
			public int compare(RevCommit c1, RevCommit c2) {
				return c1.getCommitterIdent().getWhen().compareTo(c2.getCommitterIdent().getWhen());
			}
		});
		
		/* Workaround ahead, since JGit in Windows automatically adds ^M (Carriage Returns) to some files after,
		 * leaving the working tree dirty.
		 * Neither JGit stash nor reset will work. So we need to commit!
		 * This commit doesn't affects metrics, since we do a checkout after it.
		 * To reproduce this bug, try to checkout https://github.com/playframework/ to 2012-05-01 12:00
		 * TODO: report this bug to JGit team.
		 */
		Set<String> mods = git.status().call().getModified();
		if (!mods.isEmpty()) {
			AddCommand addCmd = git.add();
			for (String m : mods) {
				addCmd.addFilepattern(m);
			}
			addCmd.call();
			git.commit().setMessage("Groundhog commit").call();
		}
		/* workaround end.*/

		git.checkout().setName("groundhog-analyze").setStartPoint(closest).setCreateBranch(true).call();
		rep.close();
	}

}