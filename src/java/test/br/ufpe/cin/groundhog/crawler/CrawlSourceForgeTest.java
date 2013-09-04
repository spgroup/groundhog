package br.ufpe.cin.groundhog.crawler;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Before;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.http.HttpModule;
import br.ufpe.cin.groundhog.http.Requests;
import br.ufpe.cin.groundhog.search.SearchModule;
import br.ufpe.cin.groundhog.search.SearchSourceForge;

import com.google.common.io.Files;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CrawlSourceForgeTest {

	private SearchSourceForge searchSourceForge;
	private Requests requests;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchSourceForge = injector.getInstance(SearchSourceForge.class);
		requests = injector.getInstance(Requests.class);
	}

//	@Test
	public void testCrawlGithub() {
		try {
			Project project = searchSourceForge.getProjects("geom-java", 1, -1).get(0);
			List<Project> projects = Arrays.asList(project);
			
			CrawlSourceForge crawl = new CrawlSourceForge(requests, Files.createTempDir());
			List<Future<File>> fs = crawl.asyncDownloadProjects(projects);
			for (Future<File> f : fs) {
				File file = f.get();
				Assert.assertNotNull(file);
			}
			
		} catch (Exception e) {
			Assert.fail();
		}
	}
}