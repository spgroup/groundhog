package br.ufpe.cin.groundhog.crawler;


import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.scmclient.GitClient;
import br.ufpe.cin.groundhog.scmclient.ScmModule;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CrawlGitHubTest {

	private GitClient gitClient;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new ScmModule());
		this.gitClient = injector.getInstance(GitClient.class);
	}

	@Test
	public void testCrawlGithub() throws InterruptedException, ExecutionException {
		CrawlGitHub crawl = new CrawlGitHub(gitClient, Files.createTempDir());
		Project p = new Project("modules.playframework.org", "description", "https://github.com/spgroup/groundhog.git");
		List<Future<File>> fs = crawl.asyncDownloadProjects(Lists.newArrayList(p));
		for (Future<File> f : fs) {
			File file = f.get();
			Assert.assertNotNull(file);
		}
	}
}