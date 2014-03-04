package br.ufpe.cin.groundhog.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gitective.core.CommitFinder;
import org.gitective.core.PathFilterUtils;
import org.gitective.core.filter.commit.AndCommitFilter;
import org.gitective.core.filter.commit.AuthorFilter;
import org.gitective.core.filter.commit.CommitCountFilter;
import org.gitective.core.filter.commit.CommitListFilter;

import br.ufpe.cin.groundhog.Commit;
import br.ufpe.cin.groundhog.User;

/**
 * Extract Commit data from Git repositories
 * @author Rodrigo Alves
 *
 */
public class GitCommitExtractor {
	
	public List<Commit> extractCommits(File project) {
		CommitListFilter list = new CommitListFilter();
		
		String path = project.getAbsolutePath() + "/.git";
		CommitFinder finder = new CommitFinder(path);
		
		finder.setFilter(list).find();
		
		for (RevCommit rev : list.getCommits()){
		    System.out.println(rev.getName() + " " + rev.getAuthorIdent().getName() + " " + rev.getShortMessage());
		}
		
		return null;
	}
	
	public List<RevCommit> getCommitList(File project) {
		CommitListFilter list = new CommitListFilter();
		
		String path = project.getAbsolutePath() + "/.git";
		CommitFinder finder = new CommitFinder(path);
		
		finder.setFilter(list).find();
//		
//		for (RevCommit rev : list.getCommits()){
//		    System.out.println(rev.getName() + " " + rev.getAuthorIdent().getName() + " " + rev.getShortMessage());
//		}
//		
		return list.getCommits();
	}
	
	/**
	 * A method that returns the number of commits that contain files with a given file extension
	 * Example usage:
	 * 
	 * File project = new File("/tmp/project");
	 * numberOfCommitsWithExtension(project, ".java") // Returns the number of commits in project that includes Java files
	 * 
	 * @param project A {@link file} object for the directory where the Git repository is located 
	 * @param extension The extension to be searched within the commits
	 * @return the number of commits that includes files with the given extension
	 */
	public int numberOfCommitsWithExtension(File project, String extension) {
		CommitCountFilter commits = new CommitCountFilter();
		String path = project.getAbsolutePath() + "/.git";
		
		CommitFinder finder = new CommitFinder(path);
		finder.setFilter(PathFilterUtils.andSuffix("." + extension));
		finder.setMatcher(commits);
		finder.find();
		
		return (int) commits.getCount();
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