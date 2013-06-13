package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.scmclient.GitClient;
import br.ufpe.cin.groundhog.scmclient.ScmModule;
import br.ufpe.cin.groundhog.search.SearchGoogleCode;
import br.ufpe.cin.groundhog.search.SearchModule;

import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CrawGoogleCodeTest {

	private SearchGoogleCode searchGoogleCode;
	private GitClient gitClient;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new ScmModule());
		searchGoogleCode = injector.getInstance(SearchGoogleCode.class);
		gitClient = injector.getInstance(GitClient.class);
	}

	@Test
	public void testCrawlGithub() {
		try {
			Project project = searchGoogleCode.getProjects("java", 1,-1).get(0);
			List<Project> projects = Arrays.asList(project);
			
			CrawlGoogleCode crawl = new CrawlGoogleCode(gitClient, Files.createTempDir());
			List<Future<File>> fs = crawl.downloadProjects(projects);
			for (Future<File> f : fs) {
				File file = f.get();
				Assert.assertNotNull(file);
			}
			
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
