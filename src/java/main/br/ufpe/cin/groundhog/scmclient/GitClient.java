package br.ufpe.cin.groundhog.scmclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AllCommitFilter;
import org.gitective.core.filter.commit.AndCommitFilter;
import org.gitective.core.filter.commit.CommitCountFilter;
import org.gitective.core.filter.commit.CommitFilter;
import org.gitective.core.filter.commit.CommitterDateFilter;

import br.ufpe.cin.groundhog.util.Dates;

/**
 * 
 * @author ghlp
 * @since 0.0.1
 */
public class GitClient {

	/**
	 * Performs a clone operation for the given project URL and places the
	 * fetched code into the destination directory.
	 * 
	 * @param url
	 *            the project's URL
	 * @param destination
	 */
	public void clone(final String url, final File destination) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {
			public void run() {
				try {
					Git git = Git.cloneRepository().setURI(url).setDirectory(destination).call();
					git.getRepository().close();
				} catch (InvalidRemoteException e) {
					e.printStackTrace();
				} catch (TransportException e) {
					e.printStackTrace();
				} catch (GitAPIException e) {
					e.printStackTrace();
				}
			}
		});
		executor.shutdown();
		
		try {
			executor.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Points to head commit on master branch
	 * 
	 * @param repositoryFolder
	 */
	public void checkout(File repositoryFolder) {
		try {
			Git git = Git.open(repositoryFolder);
			Repository repo = git.getRepository();

			CommitFinder finder = new CommitFinder(repo);
			CommitCountFilter count = new CommitCountFilter();
			finder.setFilter(new AndCommitFilter(new CommitFilter() {

				public boolean include(RevWalk walker, RevCommit cmit)
						throws IOException {
					throw StopWalkException.INSTANCE;
				}

				public RevFilter clone() {
					return this;
				}
			}, count));
			finder.find();

			System.out.println(count.getCount());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs a checkout to the given Git repository
	 * 
	 * @param repositoryFolder
	 *            the repository where the checkout will be performed
	 * @param date
	 * @throws IOException
	 * @throws RefAlreadyExistsException
	 * @throws InvalidRefNameException
	 * @throws CheckoutConflictException
	 * @throws GitAPIException
	 * @throws EmptyProjectAtDateException
	 */
	public void checkout(File repositoryFolder, Date date) throws IOException,
			RefAlreadyExistsException, InvalidRefNameException,
			CheckoutConflictException, GitAPIException,
			EmptyProjectAtDateException {

		Git git = Git.open(repositoryFolder);
		Repository rep = git.getRepository();
		CommitFinder finder = new CommitFinder(rep);
		final List<RevCommit> commits = new ArrayList<RevCommit>();

		finder.setFilter(new CommitterDateFilter(date).negate());
		AllCommitFilter filter = new AllCommitFilter() {
			@Override
			public boolean include(RevWalk walker, RevCommit commit)
					throws IOException {
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
			throw new EmptyProjectAtDateException(
					new Dates("yyyy-MM-dd").format(date));
		}

		RevCommit closest = Collections.max(commits,
				new Comparator<RevCommit>() {
					public int compare(RevCommit c1, RevCommit c2) {
						return c1.getCommitterIdent().getWhen()
								.compareTo(c2.getCommitterIdent().getWhen());
					}
				});

		/*
		 * Workaround ahead, since JGit in Windows automatically adds ^M
		 * (Carriage Returns) to some files after, leaving the working tree
		 * dirty. Neither JGit stash nor reset will work. So we need to commit!
		 * This commit doesn't affects metrics, since we do a checkout after it.
		 * To reproduce this bug, try to checkout
		 * https://github.com/playframework/ to 2012-05-01 12:00 TODO: report
		 * this bug to JGit team.
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
		/* workaround end. */

		git.checkout().setName("groundhog-analyze").setStartPoint(closest)
				.setCreateBranch(true).call();
		rep.close();
	}
}