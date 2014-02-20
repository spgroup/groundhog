package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.SCM;
import br.ufpe.cin.groundhog.scmclient.GitClient;
import br.ufpe.cin.groundhog.scmclient.ScmModule;

import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;

@Deprecated
public class CrawlGoogleCodeTest {

	private GitClient gitClient;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new ScmModule());
		gitClient = injector.getInstance(GitClient.class);
	}

	@Test
	public void testCrawlGithub() {
		try {
			Project project = new Project("fake", "fake", "http://googletransitdatafeed.googlecode.com/svn/trunk/");
			CrawlGoogleCode crawl = new CrawlGoogleCode(gitClient, Files.createTempDir());
			
			List<Future<File>> fs = crawl.asyncDownloadProjects(Arrays.asList(project));
			for (Future<File> f : fs) {
				File file = f.get();
				Assert.assertNotNull(file);
			}
			
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
