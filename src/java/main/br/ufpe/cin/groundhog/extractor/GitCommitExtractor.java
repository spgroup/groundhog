package br.ufpe.cin.groundhog.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import br.ufpe.cin.groundhog.Commit;
import br.ufpe.cin.groundhog.User;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AndCommitFilter;
import org.gitective.core.filter.commit.AuthorFilter;
import org.gitective.core.filter.commit.CommitCountFilter;
import org.gitective.core.filter.commit.CommitListFilter;

/**
 * Extract Commit data from Git repositories
 * @author Rodrigo Alves
 *
 */
public class GitCommitExtractor {
	private CommitCountFilter commits;
	
	public GitCommitExtractor() {
		this.commits = new CommitCountFilter();
	}
	
	/**
	 * 
	 * @param project
	 * @return
	 * @throws IOException
	 */
	public List<Commit> extractCommits(File project) throws IOException {
		List<Commit> commits = new ArrayList<>();
		String path = project.getAbsolutePath();
		
		CommitFinder finder = new CommitFinder(path);
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(path)).readEnvironment()
			.findGitDir()
		    .build();

		CommitListFilter list = new CommitListFilter();
		
		for (RevCommit rev : list.getCommits()){
		    System.out.println(rev.getName());
		    System.out.println(rev.getAuthorIdent().getName());
		    System.out.println(rev.getShortMessage());
		}
		
		return commits;
	}
	
	/**
	 * Extracts only the commits from a given {@link User}
	 * TODO: implement this method
	 * @return a {@link List} of {@link Commit} objects
	 * @throws IOException 
	 */
	public List<Commit> extractCommitFromUser(User user, File project) throws IOException {
		List<Commit> commits = new ArrayList<>();
		
		String path = project.getAbsolutePath();

		CommitFinder finder = new CommitFinder(path);
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(path)).readEnvironment()
			.findGitDir()
		    .build();

		CommitListFilter list = new CommitListFilter();
		AndCommitFilter filters = new AndCommitFilter();
		PersonIdent author = new PersonIdent(user.getName(), user.getEmail());

		filters.add(new AuthorFilter(author));
		return commits;
	}
}