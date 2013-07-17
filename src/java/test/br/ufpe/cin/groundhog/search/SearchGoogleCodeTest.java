package br.ufpe.cin.groundhog.search;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.http.HttpModule;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class SearchGoogleCodeTest {
	private SearchGoogleCode searchGoogleCode;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGoogleCode = injector.getInstance(SearchGoogleCode.class);
	}

	@Test
	public void testSimpleSearch() {
		List<Project> projects = searchGoogleCode.getProjects("java", 1, -1);
		Assert.assertNotNull(projects);
	}
}