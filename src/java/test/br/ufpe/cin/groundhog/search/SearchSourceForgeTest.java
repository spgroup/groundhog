package br.ufpe.cin.groundhog.search;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import br.ufpe.cin.groundhog.Project;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SearchSourceForgeTest {
	private SearchSourceForge searchSourceForge;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule());
		searchSourceForge = injector.getInstance(SearchSourceForge.class);
	}

	public void testSimpleSearch() {
		List<Project> projects = searchSourceForge.getProjects("", 1);
		Assert.assertNotNull(projects);
	}
}
