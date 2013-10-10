package br.ufpe.cin.groundhog.extractor;

import java.io.File;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.User;
import br.ufpe.cin.groundhog.crawler.CrawlGitHub;
import br.ufpe.cin.groundhog.scmclient.GitClient;

public class GitCommitExtractorTest {
	private GitCommitExtractor extractor;
	private File tempDir;
	
	@Before
	public void setup() {
		this.extractor = new GitCommitExtractor();
		this.tempDir = new File("/tmp");
	}
	
	@Test
	public void testGitCommitExtractor() {
		CrawlGitHub crawler = new CrawlGitHub(new GitClient(), this.tempDir);
		String projectName = "groundhog-case-study";
		Project pr = new Project(new User("gustavopinto"), projectName);
				
		crawler.downloadProject(pr);
		
		File project = new File(this.tempDir + "/" + projectName);
		this.extractor.extractCommits(project);
		
		// Look for Java files involved in all commits in the Groundhog Case Study
		// project, which can't be less than 1
		int javaFileCount = extractor.numberOfCommitsWithExtension(project, "java");
		assertThat(javaFileCount, not(is(0)));
	}
}