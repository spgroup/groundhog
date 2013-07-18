package br.ufpe.cin.groundhog.search;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.http.HttpModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SearchSourceForgeTest {
	private SearchSourceForge searchSourceForge;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchSourceForge = injector.getInstance(SearchSourceForge.class);
	}

	@Test
	public void testSimpleSearch() {
		List<Project> projects = searchSourceForge.getProjects("", 1, -1);
		Assert.assertNotNull(projects);
	}
}
