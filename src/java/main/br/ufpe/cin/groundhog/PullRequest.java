package br.ufpe.cin.groundhog;

import java.util.Date;

public class PullRequest extends Issue {
	private Date mergedAt;
	private String mergeCommitSha;
	
	private int reviewCommentsCount;
	private int commitsCount;
	
	private boolean merged;
	private boolean mergeable;
	
	private String mergeableState;
	
	private User mergedBy;
	
	// Code content
	private int additionsCount;
	private int deletionsCount;
	private int changedFilesCount;
  	
	public PullRequest(int number, Project project, String state) {
		super(project, number, state);
	}

	/**
	 * Returns the merge date of the Pull Request
	 * @return
	 */
	public Date getMergedAt() {
		return this.mergedAt;
	}

	public void setMergedAt(Date mergedAt) {
		this.mergedAt = mergedAt;
	}

	/**
	 * Returns the SHA String of the merge commit
	 * @return
	 */
	public String getMergeCommitSha() {
		return this.mergeCommitSha;
	}

	public void setMergeCommitSha(String mergeCommitSha) {
		this.mergeCommitSha = mergeCommitSha;
	}

	/**
	 * Returns the sum of comments written in the code diffs of commits belonging to the Pull Request
	 * @return
	 */
	public int getReviewCommentsCount() {
		return this.reviewCommentsCount;
	}

	public void setReviewCommentsCount(int reviewCommentsCount) {
		this.reviewCommentsCount = reviewCommentsCount;
	}

	/**
	 * Returns the sum of all commits contained in the Pull Request
	 * @return
	 */
	public int getCommitsCount() {
		return this.commitsCount;
	}

	public void setCommitsCount(int commitsCount) {
		this.commitsCount = commitsCount;
	}

	/**
	 * Returns true if the PR has been merged. Returns false otherwise.
	 * @return
	 */
	public boolean isMerged() {
		return this.merged;
	}

	public void setMerged(boolean merged) {
		this.merged = merged;
	}

	/**
	 * Indicates whether a PR can be automatically merged by GitHub or the merge can only be done
	 * by a human (in case of conflict).
	 * @return
	 */
	public boolean isMergeable() {
		return this.mergeable;
	}

	public void setMergeable(boolean mergeable) {
		this.mergeable = mergeable;
	}

	/**
	 * Indicates the "mergeable" state of a Pull Request,
	 * Which determines whether GitHub itself can perform a merge of a PR (when the PR implies no conflicts)
	 * or only a human developer.
	 * 
	 * Possible returns are "clean" and "unknown"
	 * @return
	 */
	public String getMergeableState() {
		return this.mergeableState;
	}

	public void setMergeableState(String mergeableState) {
		this.mergeableState = mergeableState;
	}

	/**
	 * Returns the User who has merged a Pull Request
	 * @return
	 */
	public User getMergedBy() {
		return this.mergedBy;
	}

	public void setMergedBy(User mergedBy) {
		this.mergedBy = mergedBy;
	}

	/**
	 * Indicates the sum of additions in LoC that happened throught the commits of a Pull Request
	 * @return
	 */
	public int getAdditionsCount() {
		return this.additionsCount;
	}

	public void setAdditionsCount(int additionsCount) {
		this.additionsCount = additionsCount;
	}

	/**
	 * Indicates the sum of deletions in LoC that happened throught the commits of a Pull Request
	 * @return
	 */
	public int getDeletionsCount() {
		return this.deletionsCount;
	}

	public void setDeletionsCount(int deletionsCount) {
		this.deletionsCount = deletionsCount;
	}

	/**
	 * Indicates how many files were modified in a Pull Request in respect to the
	 * branch where the merge is supposed to happen
	 * @return
	 */
	public int getChangedFilesCount() {
		return this.changedFilesCount;
	}

	public void setChangedFilesCount(int changedFilesCount) {
		this.changedFilesCount = changedFilesCount;
	}

	@Override
	public String toString() {
		return "PullRequest: number = " + this.getNumber() + ", "
				+ (this.getTitle() != null ? "title = " + this.getTitle() + ", " : "")
				+ (this.getURL() != null ? "URL = " + this.getURL() : "");
	}
}