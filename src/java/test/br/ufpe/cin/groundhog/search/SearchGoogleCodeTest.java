package br.ufpe.cin.groundhog.search;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpe.cin.groundhog.Project;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SearchGoogleCodeTest {

	private SearchGoogleCode searchGoogleCode;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule());
		searchGoogleCode = injector.getInstance(SearchGoogleCode.class);
	}

	@Test
	public void testSimpleSearch() {
		List<Project> projects = searchGoogleCode.getProjects("java", 1);
		Assert.assertNotNull(projects);
	}
}
