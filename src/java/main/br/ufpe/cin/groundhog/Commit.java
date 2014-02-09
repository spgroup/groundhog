package br.ufpe.cin.groundhog;

import java.util.Date;
import java.util.List;

import br.ufpe.cin.groundhog.util.Dates;

import com.google.gson.annotations.SerializedName;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

/**
 * Represents a Commit object in Groundhog
 * @author Rodrigo Alves, gustavopinto
 * @since 0.0.1
 */

@Entity("commits")
public class Commit extends GitHubEntity {
	@SerializedName("sha")
    @Indexed(unique=true, dropDups=true)
	private String sha;
	
	@SerializedName("commiter")
	@Reference private User commiter;
	
	@SerializedName("message")
	private String message;
	
	@Reference private Project project;
	
	private Date commitDate;
	
	private CommitStats stats;

	private List<CommitFile> files;

	private List<CommitParent> parents;
	
	public Commit(String sha, Project project) {
		this.sha = sha;
		this.project = project;
	}
	
	/**
	 * Informs the SHA checksum of the commit
	 * @return
	 */
	public String getSha() {
		return this.sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	/**
	 * Informs the User who authored the commit
	 * @return
	 */
	public User getCommiter() {
		return this.commiter;
	}

	public void setCommiter(User commiter) {
		this.commiter = commiter;
	}

	/**
	 * Informs the commit message
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Informs the project to which the commit belongs
	 * @return a {@link Project} object
	 */
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Informs the date when the commit was done 
	 * @return a {@link Date} object
	 */
	public Date getCommitDate() {
		return this.commitDate;
	}

	public void setCommitDate(String date) {
		Date createAtDate = new Dates("yyyy-MM-dd HH:mm:ss").format(date.replaceAll("T", " ").replace("Z", ""));
		this.commitDate = createAtDate;
	}

	/**
	 * Gives the abbreviated SHA of the {@link Commit} object
	 * @return a {@link String} object
	 */
	public String getabbrevSHA() {
		return this.sha.substring(0, 7);
	}

	public List<CommitFile> getFiles() {
		return this.files;
	}

	public CommitStats getStats() {
		return this.stats;
	}

	public List<CommitParent> getParents() {
		return this.parents;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sha == null) ? 0 : sha.hashCode());
		return result;
	}

	/**
	 * Two {@link Commit} objects are considered equal if and only if both have the same SHA hash
	 * @param commit
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Commit other = (Commit) obj;
		if (sha == null) {
			if (other.sha != null)
				return false;
		} else if (!sha.equals(other.sha))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Commit [" + (sha != null ? "sha=" + sha + ", " : "")
				+ (commiter != null ? "commiter=" + commiter + ", " : "")
				+ (message != null ? "message=" + message + ", " : "")
				+ (project != null ? "project=" + project + ", " : "")
				+ (commitDate != null ? "commitDate=" + commitDate : "") + "]";
	}

	@Override
	public String getURL() {
		return String.format("https://api.github.com/repos/%s/%s/commits/%s",
				this.getProject().getUser().getLogin(),
				this.getProject().getName(),
				this.sha);
	}

	/**
	 * The commit changes stats, i.e., number of additions, deletions and total
	 * @author Irineu
	 *
	 */
	public static final class CommitStats {
		private int additions;

		private int deletions;

		private int total;

		public int getAdditions() {
			return additions;
		}

		public int getDeletions() {
			return deletions;
		}

		public int getTotal() {
			return total;
		}

		@Override
		public String toString() {
			return "CommitStats [additions=" + additions + ", deletions="
					+ deletions + ", total=" + total + "]";
		}

	}

	public static final class CommitFile {
		@SerializedName(value="filename")
		private String fileName;

		@SerializedName(value="additions")
		private int additionsCount;

		@SerializedName(value="deletions")
		private int deletionsCount;

		@SerializedName(value="changes")
		private int changesCount;

		@SerializedName(value="status")
		private String status;

		@SerializedName(value="blob_url")
		private String blobUrl;

		@SerializedName(value="patch")
		private String patch;

		public String getFileName() {
			return fileName;
		}

		public int getAdditionsCount() {
			return additionsCount;
		}

		public int getDeletionsCount() {
			return deletionsCount;
		}

		public int getChangesCount() {
			return changesCount;
		}

		public String getStatus() {
			return status;
		}

		public String getBlobUrl() {
			return blobUrl;
		}

		public String getPatch() {
			return patch;
		}

		@Override
		public String toString() {
			return "CommitFile [fileName=" + fileName + ", additionsCount="
					+ additionsCount + ", deletionsCount=" + deletionsCount
					+ ", changesCount=" + changesCount + ", status=" + status
					+ ", patch=" + patch + "]";
		}
	}

	public static class CommitParent {

		private String url;

		private String sha;

		public String getUrl() {
			return url;
		}

		public String getSha() {
			return sha;
		}

		@Override
		public String toString() {
			return "CommitParent [url=" + url + ", sha=" + sha + "]";
		}
	}
}
