package br.ufpe.cin.groundhog.search;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import br.ufpe.cin.groundhog.Project;
import br.ufpe.cin.groundhog.http.HttpModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

@Deprecated
public class SearchGoogleCodeTest {
	private SearchGoogleCode searchGoogleCode;

	@Before
	public void setup() {
		Injector injector = Guice.createInjector(new SearchModule(), new HttpModule());
		searchGoogleCode = injector.getInstance(SearchGoogleCode.class);
	}

	public void testSimpleSearch() {
		List<Project> projects = searchGoogleCode.getProjects("java", 1, -1);
		Assert.assertNotNull(projects);
	}
}